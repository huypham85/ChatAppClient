package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.model.Message
import kotlinx.coroutines.flow.MutableSharedFlow

interface MessageRepository {
    suspend fun receiveNewMessage(message: Message)
    val newMessageReceive: MutableSharedFlow<Message>
}

class MessageRepositoryImpl : MessageRepository {

    private var _newMessageReceive = MutableSharedFlow<Message>()
    override val newMessageReceive: MutableSharedFlow<Message>
        get() = _newMessageReceive

    override suspend fun receiveNewMessage(message: Message) {
        _newMessageReceive.emit(message)
    }




}