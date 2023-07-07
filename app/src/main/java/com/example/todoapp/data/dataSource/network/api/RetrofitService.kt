package com.example.todoapp.data.dataSource.network.api

import com.example.todoapp.data.dataSource.network.dto.responses.GetListApiResponse
import com.example.todoapp.data.dataSource.network.dto.requests.PatchListApiRequest
import com.example.todoapp.data.dataSource.network.dto.requests.PostItemApiRequest
import com.example.todoapp.data.dataSource.network.dto.responses.PostItemApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitService {


    @GET("list")
    suspend fun getList(
        @Header("Authorization") token: String,
    ): GetListApiResponse

    @PATCH("list")
    suspend fun updateList(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: PatchListApiRequest
    ): GetListApiResponse

    @POST("list")
    suspend fun postElement(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body element: PostItemApiRequest
    ): PostItemApiResponse

    @DELETE("list/{id}")
    suspend fun deleteElement(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
    ): PostItemApiResponse

    @PUT("list/{id}")
    suspend fun updateElement(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body item: PostItemApiRequest
    ): PostItemApiResponse


}
