package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.model.Message
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

interface MessageRepository {
    suspend fun receiveNewText(text: String)
    suspend fun receiveNewMessage(message: Message)
    val newMessageReceive: MutableSharedFlow<Message>

    val receiveText: MutableSharedFlow<String>

    val idRoomReceive:MutableSharedFlow<String>
}

@Singleton
class MessageRepositoryImpl @Inject constructor() : MessageRepository {

    private var _newMessageReceive = MutableSharedFlow<Message>()
    override val newMessageReceive: MutableSharedFlow<Message>
        get() = _newMessageReceive

    private var _idRoomReceive = MutableSharedFlow<String>()
    override val idRoomReceive: MutableSharedFlow<String>
        get() = _idRoomReceive

    private var _receiveText = MutableSharedFlow<String>()
    override val receiveText: MutableSharedFlow<String>
        get() = _receiveText

    override suspend fun receiveNewText(text: String) {
        _receiveText.emit(text)
    }

    override suspend fun receiveNewMessage(message: Message) {
        _newMessageReceive.emit(message)
        _idRoomReceive.emit(message.roomId)
    }

}