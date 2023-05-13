package com.vn.chat_app_client.data.api.auth.response.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("_id")
    val userId: String,
    val username: String,
    val avatar: String?,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
)
