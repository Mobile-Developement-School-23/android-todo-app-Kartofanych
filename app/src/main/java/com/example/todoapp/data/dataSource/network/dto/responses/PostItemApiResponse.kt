package com.example.todoapp.data.dataSource.network.dto.responses

import com.google.gson.annotations.SerializedName

data class PostItemApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val item: TodoItemResponse,


    @SerializedName("revision")
    val revision: Int

)
