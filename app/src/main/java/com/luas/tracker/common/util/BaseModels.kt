package com.luas.tracker.common.util

sealed class LoadingState(val showInfo: Boolean, val showError: Boolean) {
    object InProgress : LoadingState(showInfo = false, showError = false)
    object Success : LoadingState(showInfo = true, showError = false)
    object Error : LoadingState(showInfo = false, showError = true)
}

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    class Error<out T>(val throwable: Throwable) : ApiResponse<T>()
}