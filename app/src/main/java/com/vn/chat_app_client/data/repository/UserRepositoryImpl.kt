package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.service.UserService
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val service: UserService,
) : UserRepository {

    override suspend fun listUsers():  Result<List<User>> {
        return withContext(Dispatchers.Default) {
            try {
                val response = service.getUsers(1,10)
                Result.success(response)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }
}