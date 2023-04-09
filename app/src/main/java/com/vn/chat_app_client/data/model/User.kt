package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class User(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val password: String,
    val lastname: String,
    val firstname: String
) : Serializable