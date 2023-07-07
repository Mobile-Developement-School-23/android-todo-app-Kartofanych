package com.example.todoapp.data.dataSource.network.dto.requests

import com.example.todoapp.data.dataSource.network.dto.responses.TodoItemResponse
import com.google.gson.annotations.SerializedName

data class PostItemApiRequest(
    @SerializedName("element")
    val item: TodoItemResponse,
)
