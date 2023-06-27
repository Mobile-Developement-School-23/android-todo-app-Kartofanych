package com.example.todoapp.network.responses

import com.google.gson.annotations.SerializedName

data class GetListApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemResponse>,


    @SerializedName("revision")
    val revision: Int

)

