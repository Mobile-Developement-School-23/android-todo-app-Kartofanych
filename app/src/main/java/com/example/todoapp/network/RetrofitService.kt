package com.example.todoapp.network

import com.example.todoapp.network.responses.TodoItemResponse
import com.example.todoapp.network.responses.GetListApiResponse
import com.example.todoapp.network.responses.PatchListApiRequest
import com.example.todoapp.network.responses.PostItemApiRequest
import com.example.todoapp.network.responses.PostItemApiResponse
import com.example.todoapp.room.TodoItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {
    @GET("list")
    fun getList():Call<GetListApiResponse>
    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: PatchListApiRequest
    ): Response<GetListApiResponse>
    @POST("list")
    suspend fun postElement(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body element: PostItemApiRequest
    ): Response<PostItemApiResponse>
    @DELETE("list/{id}")
    suspend fun deleteElement(
        @Path("id") id:String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
    ): Response<PostItemApiResponse>

    @POST("list")
    fun addElement(todoItem: TodoItem):Call<String>

}