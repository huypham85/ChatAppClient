package com.vn.chat_app_client.data.model

data class Message(
    val senderId: Int,
    val message: String,
    val time: Long,
)
