package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository

class AuthRepositoryMock : AuthRepository {
    override suspend fun checkLogin(user: User): Result<LoginResponse?> {
        return Result.success(LoginResponse(token = ""))
    }
}