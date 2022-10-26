package com.vn.chat_app_client.data.api.auth.response

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val username: String,
    val password: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
)
