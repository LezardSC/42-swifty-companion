package com.fortytwo.swifty.ui.common

enum class ErrorKind { NOT_FOUND, NETWORK, UNKNOWN }

sealed interface UiState<out T> {
    data object Idle: UiState<Nothing>
    data object Loading: UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val kind: ErrorKind): UiState<Nothing>
}