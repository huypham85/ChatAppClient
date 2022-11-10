package com.vn.chat_app_client.data.model

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("_id")
    val id: String,
    val name: String,
    @SerializedName("lastMessage")
    val lastMessage: LastMessage? = null
) : java.io.Serializable

data class LastMessage(
    val _id: String,
    val id: String,
    val text: String,
    val attachments: List<String>,
    val senderId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
) : java.io.Serializable

