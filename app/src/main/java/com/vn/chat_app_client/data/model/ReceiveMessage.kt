package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName

data class ReceiveMessage(
    val text: String,
    val sender: Sender,
    val attachments: List<Any?>,
    val createdAt: String,
    val roomId: String,
    var type: MessageType? = null,
)

data class Sender(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val password: String,
    val lastName: String,
    val firstName: String,
)
