package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarRequest

interface ProfileRepository {
    suspend fun getProfile(): Result<ProfileResponse>
    suspend fun updateAvatar(userId:String, updateAvatarRequest: UpdateAvatarRequest): Result<ProfileResponse>
}