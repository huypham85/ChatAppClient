package com.vn.chat_app_client.data.api.auth.response.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("userId")
    val userId: String,
    val username: String,
)
