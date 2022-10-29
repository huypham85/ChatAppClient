package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryMock @Inject constructor(
    private val savedAccountManager: SavedAccountManager,
) : AuthRepository {
    override suspend fun checkLogin(user: User): Result<LoginResponse> {
        return Result.success(LoginResponse(accessToken = ""))
    }

    override fun saveAccount(loginData: LoginResponse): Result<Unit> {
        savedAccountManager.saveAuthToken(loginData.accessToken)
        return Result.success(Unit)
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return Result.success(RegisterResponse("", "", "", ""))
    }
}