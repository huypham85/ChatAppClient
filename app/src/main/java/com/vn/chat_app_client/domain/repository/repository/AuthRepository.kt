package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.data.model.User

interface AuthRepository {
    suspend fun checkLogin(user: User): Result<LoginResponse>
    fun saveAccount(loginData: LoginResponse): Result<Unit>
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
}