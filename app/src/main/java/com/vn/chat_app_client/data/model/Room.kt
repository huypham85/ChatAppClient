package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("_id")
    val id: String,
    val name: String
) : java.io.Serializable