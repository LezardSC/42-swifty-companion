package com.fortytwo.swifty.data.auth

import com.fortytwo.swifty.data.network.AuthApiService
import com.fortytwo.swifty.BuildConfig
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


object TokenManager {
    private const val SAFETY_MARGIN_SECONDS = 60L

    private val mutex = Mutex()

    private data class CachedToken(val value: String, val expiresAtEpochSeconds: Long)
    private var cached: CachedToken? = null

    suspend fun getToken(authApi: AuthApiService, forceRefresh: Boolean = false): String = mutex.withLock {
        val now = System.currentTimeMillis() / 1000
        val current = cached
        if (!forceRefresh && current != null && now < current.expiresAtEpochSeconds - SAFETY_MARGIN_SECONDS) {
            return@withLock current.value
        }
        val response = authApi.getToken(
                grantType = "client_credentials",
                clientId = BuildConfig.API_UID,
                clientSecret = BuildConfig.API_SECRET
        )
        val fresh = CachedToken(response.accessToken, now + response.expiresIn)
        // for testing purposes
        // val fresh = CachedToken(response.accessToken, now + 5)
        cached = fresh
        fresh.value
    }
}