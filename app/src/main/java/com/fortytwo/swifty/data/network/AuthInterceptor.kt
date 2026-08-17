package com.fortytwo.swifty.data.network

import com.fortytwo.swifty.data.auth.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (
    private val authApi: AuthApiService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { TokenManager.getToken(authApi) }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            val freshToken = runBlocking { TokenManager.getToken(authApi, forceRefresh = true) }
            val retried = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $freshToken")
                .build()

            return chain.proceed(retried)
        }
        return response
    }
}