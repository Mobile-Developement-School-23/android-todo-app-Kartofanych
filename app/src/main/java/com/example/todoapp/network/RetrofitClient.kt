package com.example.todoapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private var retrofitClient: Retrofit? = null

    fun getClient(baseUrl:String):Retrofit{
        if(retrofitClient == null){
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer unaffordable")
                    .build()
                chain.proceed(newRequest)
            }.build()
            retrofitClient = Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitClient!!
    }
}