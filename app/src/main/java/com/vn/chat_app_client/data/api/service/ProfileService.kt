package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileService {

    @GET("users/profile")
    suspend fun getProfile(): ProfileResponse

    @PATCH("users/{userId}")
    suspend fun updateAvatar(
        @Path("userId") userId: String,
        @Body updateAvatarRequest: UpdateAvatarRequest
    ): ProfileResponse
}