package com.vn.chat_app_client.data.api.room

import com.google.gson.annotations.SerializedName

data class CreateRoomResponse(
    @SerializedName("_id")
    val id: String,
    val members: List<String>,
    val adminId: String,
    val messages: List<Message>,
    val createdAt: String,
)

data class Message(
    val text: String,
    val senderId: String,
    val createdAt: String,
    @SerializedName("_id")
    val id: String,
)

