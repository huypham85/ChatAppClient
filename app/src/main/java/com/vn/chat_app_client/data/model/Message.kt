package com.vn.chat_app_client.data.model

data class Message(
    val id: String,
    val text: String,
    val attachments: String,
    val senderId: String,
    val roomId: String
) : java.io.Serializable