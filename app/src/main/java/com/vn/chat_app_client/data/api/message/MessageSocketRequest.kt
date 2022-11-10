package com.vn.chat_app_client.data.api.message

import com.google.gson.annotations.SerializedName

data class MessageSocketRequest(
    @SerializedName("roomId")
    val roomId: String,
    val text: String,
    @SerializedName("attachments")
    val attachments: List<String>? = null,
)
