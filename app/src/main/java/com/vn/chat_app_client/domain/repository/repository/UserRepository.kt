package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.model.User

interface UserRepository {
    suspend fun listUsers(): Result<List<User>>
}