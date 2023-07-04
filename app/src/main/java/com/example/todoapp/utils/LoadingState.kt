package com.example.todoapp.utils


sealed class NetworkState <out T> {
    object Loading : NetworkState<Nothing>()
    data class Success<T>(val data: T, val revision: Int) : NetworkState<T>()
    data class Failure(val cause: Throwable): NetworkState<Nothing>()
}