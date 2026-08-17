package com.fortytwo.swifty.data.repository

import retrofit2.HttpException
import com.fortytwo.swifty.data.model.IntraUser
import com.fortytwo.swifty.data.network.IntraApiService
import com.fortytwo.swifty.ui.common.ErrorKind
import com.fortytwo.swifty.ui.common.UiState
import okio.IOException

class IntraRepository(private val api: IntraApiService) {
    suspend fun findUser(login: String): UiState<IntraUser> {
        val trimmed = login.trim()
        if (trimmed.isEmpty()) {
            return UiState.Error("Enter a login to search.", ErrorKind.UNKNOWN)
        }
        return try {
            UiState.Success(api.getUser(trimmed.lowercase()))
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> UiState.Error("No student found for \"$trimmed\".", ErrorKind.NOT_FOUND)
                401 -> UiState.Error("Authentication with the 42 API failed.", ErrorKind.UNKNOWN)
                else -> UiState.Error("42 API error (${e.code()}).", ErrorKind.UNKNOWN)
            }
        } catch (e: IOException) {
            UiState.Error("Can't reach the 42 API. Check your connection.", ErrorKind.NETWORK)
        }
    }
}