package com.example.todoapp.network

object Common {

    private val baseURL:String = "https://beta.mrdekk.ru/todobackend/"
    val retrofitService:RetrofitService get() = RetrofitClient.getClient(baseURL).create(RetrofitService::class.java)

}