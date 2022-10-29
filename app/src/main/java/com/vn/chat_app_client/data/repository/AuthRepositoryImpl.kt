package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.data.api.common.AccountData
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.service.AuthService
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import com.vn.chat_app_client.utils.JWTHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val savedAccountManager: SavedAccountManager,
) : AuthRepository {
    override suspend fun checkLogin(user: User): Result<LoginResponse> {
        return withContext(Dispatchers.Default) {
            try {
                val body = LoginRequest(user.userName, user.password)
                val response = service.checkLogin(body)
                Result.success(response)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

    override fun saveAccount(loginData: LoginResponse): Result<Unit> {
        return try {
            val accountData = JWTHelper.decode<AccountData>(loginData.accessToken)
            // TODO: handle data from accountData
            savedAccountManager.saveAuthToken(loginData.accessToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return withContext(Dispatchers.Default) {
            try {
                val response = service.register(registerRequest)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}