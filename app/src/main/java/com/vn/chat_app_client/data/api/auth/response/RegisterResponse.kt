package com.vn.chat_app_client.data.api.auth.response

import java.io.Serializable

data class RegisterResponse(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
) : Serializable
