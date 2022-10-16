package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.service.AuthService
import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
) : AuthRepository {
    override suspend fun checkLogin(user: User): Result<LoginResponse?> {
        return withContext(Dispatchers.Default) {
            try {
                val body = LoginRequest(user.userName, user.password)
                val response = service.checkLogin(body)
                Result.success(response.data)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

}