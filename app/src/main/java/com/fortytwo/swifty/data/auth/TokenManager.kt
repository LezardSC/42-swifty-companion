package com.fortytwo.swifty.data.auth

import com.fortytwo.swifty.data.network.AuthApiService
import android.os.SystemClock
import com.fortytwo.swifty.BuildConfig
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object TokenManager {
    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var expiresAt: Long = 0

    suspend fun getToken(authApi: AuthApiService): String = mutex.withLock {
        cachedToken?.takeIf { SystemClock.elapsedRealtime() < expiresAt } ?: run {
            val response = authApi.getToken(
                grantType = "client_credentials",
                clientId = BuildConfig.API_UID,
                clientSecret = BuildConfig.API_SECRET
            )
            expiresAt = SystemClock.elapsedRealtime() + (response.expiresIn - 60) * 1000L
            response.accessToken.also { cachedToken = it }
        }
    }
}