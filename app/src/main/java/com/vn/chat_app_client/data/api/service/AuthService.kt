package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun checkLogin(
        @Body user: LoginRequest
    ): LoginResponse

    @POST("/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse
}