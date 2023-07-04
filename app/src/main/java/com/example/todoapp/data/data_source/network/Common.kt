package com.example.todoapp.data.data_source.network

object Common {

    var token = "no_token"
        set(value) {
            field = value
            RetrofitClient.token = value
        }
    var phoneID = "id"
    val retrofitService: RetrofitService =
        RetrofitClient.createClient().create(RetrofitService::class.java)


}