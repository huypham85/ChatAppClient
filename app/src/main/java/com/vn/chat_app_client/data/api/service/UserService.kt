package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("/users")
    suspend fun getUsers(
        @Query("size") size: Int,
        @Query("page") page: Int,
    ): List<User>

    @GET("/users")
    suspend fun getUser(
        @Query("id") id: String,
    ): List<User>

}