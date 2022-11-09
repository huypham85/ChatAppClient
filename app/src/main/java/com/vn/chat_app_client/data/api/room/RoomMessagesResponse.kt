package com.vn.chat_app_client.data.api.room

import com.google.gson.annotations.SerializedName
import com.vn.chat_app_client.data.model.RoomMessage


data class RoomMessagesResponse (
    @SerializedName("_id")
    val id: String,
    val members: List<AdminId>,
    @SerializedName("adminId")
    val adminId: AdminId,
    val name: String,
    val messages: List<RoomMessage>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("lastMessage")
    val lastMessage: RoomMessage
)

data class AdminId (
    @SerializedName("_id")
    val id: String,
    val username: String
)
