package com.vn.chat_app_client.data.api.response.loginresponse

import java.io.Serializable

data class LoginResponse(
    val message: String,
    val status: Int,
    val token: String
): Serializable