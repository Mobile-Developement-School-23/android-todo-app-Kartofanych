package com.example.todoapp.utils

sealed class LoadingState<out T> {
    data class Error<T>(val error: String) : LoadingState<T>()
    data class Loading<out T>(val isLoading: Boolean) : LoadingState<T>()
    data class Success<out T>(val data: T) : LoadingState<T>()
}