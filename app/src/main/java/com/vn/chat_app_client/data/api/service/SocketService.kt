package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.model.Message

interface SocketService {
    fun startListening(): Result<Unit>
    fun stopListening()
    fun sendMessage(message: Message)
}