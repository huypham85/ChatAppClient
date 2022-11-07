package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import retrofit2.http.GET

interface ProfileService {

    @GET("auth/profile")
    suspend fun getProfile(): ProfileResponse
}