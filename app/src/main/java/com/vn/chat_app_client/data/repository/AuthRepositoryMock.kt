package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.common.SavedAccount
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryMock @Inject constructor(
    private val savedAccount: SavedAccount,
) : AuthRepository {
    override suspend fun checkLogin(user: User): Result<LoginResponse> {
        return Result.success(LoginResponse(token = ""))
    }

    override fun saveAccount(loginData: LoginResponse): Result<Unit> {
        savedAccount.accessToken = loginData.token
        return Result.success(Unit)
    }
}