package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile() : Result<ProfileResponse>
}