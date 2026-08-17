package com.fortytwo.swifty.data.network

import com.fortytwo.swifty.data.model.IntraUser
import retrofit2.http.GET
import retrofit2.http.Path

interface IntraApiService {
    @GET("v2/users/{login}")
    suspend fun getUser(@Path("login") login: String): IntraUser
}