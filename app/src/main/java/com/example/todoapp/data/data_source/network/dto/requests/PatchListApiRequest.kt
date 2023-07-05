package com.example.todoapp.data.data_source.network.dto.requests

import com.example.todoapp.data.data_source.network.dto.responses.TodoItemResponse
import com.google.gson.annotations.SerializedName

data class PatchListApiRequest(
    @SerializedName("list")
    val list: List<TodoItemResponse>
)