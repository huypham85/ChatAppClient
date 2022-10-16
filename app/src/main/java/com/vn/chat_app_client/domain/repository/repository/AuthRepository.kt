package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.model.User

interface AuthRepository {
    suspend fun checkLogin(user: User): Result<LoginResponse?>
}