package com.example.todoapp.network

object Common {

    private const val baseURL:String = "https://beta.mrdekk.ru/todobackend/"
    const val updated_by = "1"
    val retrofitService:RetrofitService get() = RetrofitClient.getClient(baseURL).create(RetrofitService::class.java)

}