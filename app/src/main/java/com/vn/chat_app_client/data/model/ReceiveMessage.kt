package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName

data class ReceiveMessage(
    val text: String,
    val sender: Sender,
    val attachments: List<Attachment?>,
    val createdAt: String,
    val roomId: String,
)

data class Attachment(
    @SerializedName("_id")
    val id: String,
    val filename: String,
    val size: Long,
)

data class Sender(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val lastName: String,
    val firstName: String,
)
