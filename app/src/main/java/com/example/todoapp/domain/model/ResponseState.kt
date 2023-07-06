package com.example.todoapp.domain.model

sealed class ResponseState {
    object Loading : ResponseState()
    object Success : ResponseState()
    data class Exception(val cause: Throwable): ResponseState()
}