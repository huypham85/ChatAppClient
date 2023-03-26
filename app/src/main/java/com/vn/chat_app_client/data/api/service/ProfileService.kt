package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarRequest
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarResponse
import retrofit2.http.*

interface ProfileService {

    @GET("users/profile")
    suspend fun getProfile(): ProfileResponse

    @PATCH("users/{userId}")
    suspend fun updateAvatar(@Path("userId") userId: String, @Body updateAvatarRequest: UpdateAvatarRequest): ProfileResponse
}