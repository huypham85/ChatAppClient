package com.vn.chat_app_client.data.model

import java.io.Serializable


data class User (
    val userName: String,
    val password: String,
) : Serializable