package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse

interface AuthRepository {
    suspend fun checkLogin(user: LoginRequest): Result<LoginResponse>
    fun saveAccount(loginData: LoginResponse): Result<Unit>
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
}