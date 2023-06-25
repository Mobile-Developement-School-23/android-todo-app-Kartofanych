package com.example.todoapp.network

import com.example.todoapp.room.TodoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface RetrofitService {
    @GET("")
    fun getServer(): Call<String>

    @GET("/list")
    fun getList():Call<List<TodoItem>>

    @PATCH("/list")
    fun updateList(list: List<TodoItem>):Call<String>

    @POST("/list")
    fun addElement(todoItem: TodoItem):Call<String>

}