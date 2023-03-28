package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarRequest
import com.vn.chat_app_client.data.api.service.ProfileService
import com.vn.chat_app_client.domain.repository.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val service: ProfileService,
) : ProfileRepository {
    override suspend fun getProfile(): Result<ProfileResponse> {
        return try {
            val response = service.getProfile()
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun updateAvatar(userId:String, updateAvatarRequest: UpdateAvatarRequest): Result<ProfileResponse> {
        return try {
            val response = service.updateAvatar(userId, updateAvatarRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}