package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.model.Message


interface EventListener {
    fun onConnect()
    fun onConnectionError()
    fun onReconnect()
    fun onDisconnect()
    fun onNewMessage(message: Message)
}