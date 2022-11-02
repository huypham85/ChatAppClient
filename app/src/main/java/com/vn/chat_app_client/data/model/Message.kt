package com.vn.chat_app_client.data.model

import android.net.Uri

enum class MessageType {
    PHOTO, TEXT
}

data class Message(
    val id: String,
    val text: String,
    val attachment: String,
    val photoUri: Uri? = null,
    val senderId: String,
    val roomId: String,
    val type: MessageType,
) : java.io.Serializable