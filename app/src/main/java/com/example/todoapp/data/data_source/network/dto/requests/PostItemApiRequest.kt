package com.example.todoapp.data.data_source.network.dto.requests

import com.example.todoapp.data.data_source.network.dto.responses.TodoItemResponse
import com.google.gson.annotations.SerializedName

data class PostItemApiRequest(
    @SerializedName("element")
    val item: TodoItemResponse,
)
