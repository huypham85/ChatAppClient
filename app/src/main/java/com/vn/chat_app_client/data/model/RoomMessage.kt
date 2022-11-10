package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName

data class RoomMessage(
    val text: String,
    @SerializedName("senderId")
    val senderId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    val attachments: List<Attachment?>,
    var type: MessageType? = null,
    var senderName: String? = null,
)
