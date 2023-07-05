package com.example.todoapp.utils

import com.example.todoapp.data.data_source.network.RetrofitClient
import com.example.todoapp.data.data_source.network.api.RetrofitService

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