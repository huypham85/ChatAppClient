package com.vn.chat_app_client.data.api.auth.response.profile

import com.google.gson.annotations.SerializedName

data class UpdateAvatarResponse(
    @SerializedName("_id")
    val id: String,
    val avatar: String?,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    val username: String
)