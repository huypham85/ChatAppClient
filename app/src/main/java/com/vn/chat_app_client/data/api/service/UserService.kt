package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.data.model.User
import retrofit2.http.*

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