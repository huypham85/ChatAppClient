package com.vn.chat_app_client.data.model

import java.io.Serializable


data class User (
    val username: String,
    val password: String,
    val lastname:String,
    val firstname:String
) : Serializable