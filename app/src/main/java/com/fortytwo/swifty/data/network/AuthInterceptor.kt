package com.fortytwo.swifty.data.network

import com.fortytwo.swifty.data.auth.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class AuthConfigException(message: String, cause: Throwable) : IOException(message, cause)

class AuthInterceptor (
    private val authApi: AuthApiService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = fetchToken(forceRefresh = false)
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            val freshToken = fetchToken(forceRefresh = true)
            val retried = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $freshToken")
                .build()

            return chain.proceed(retried)
        }
        return response
    }

    private fun fetchToken(forceRefresh: Boolean): String {
        try {
            return runBlocking { TokenManager.getToken(authApi, forceRefresh) }
        } catch (e: Exception) {
            throw AuthConfigException("Could not authenticate with the 42 API. Check your API credentials.", e)
        }
    }
}