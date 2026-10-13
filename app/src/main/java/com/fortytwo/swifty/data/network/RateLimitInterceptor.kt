package com.fortytwo.swifty.data.network

import okhttp3.Interceptor
import okhttp3.Response

// The 42 API allows 2 requests per second: on a 429, wait as told and retry once
class RateLimitInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code != 429) return response

        val waitSeconds = response.header("Retry-After")?.toLongOrNull() ?: DEFAULT_WAIT_SECONDS
        response.close()
        Thread.sleep(waitSeconds.coerceIn(1, MAX_WAIT_SECONDS) * 1000)
        return chain.proceed(chain.request())
    }

    private companion object {
        const val DEFAULT_WAIT_SECONDS = 1L
        const val MAX_WAIT_SECONDS = 5L
    }
}
