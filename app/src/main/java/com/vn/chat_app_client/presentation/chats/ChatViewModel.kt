package com.vn.chat_app_client.presentation.chats

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.MessageType
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.presentation.home.HomeFragment.Companion.ROOM_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketRepository: SocketRepositoryImpl,
    messageRepository: MessageRepository,
    private val roomRepository: RoomRepository,
    private val savedAccountManager: SavedAccountManager,
) : ViewModel() {
    val messageResponse = messageRepository.newMessageReceive
    private var roomId : String? = ""

    init {
//        socketRepository.startListening()
    }

    var messageText = MutableStateFlow("")
    private val value = mutableListOf<Message>()
    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages = _messages.asStateFlow()

    fun sendNewMessage(messageText: String) {
        if (messageText.isNotBlank()) {
            socketRepository.sendMessage(
                MessageSocketRequest(
                    roomId ?: "",
                    messageText
                )
            )
        }
        this.messageText.value = ""
    }

    fun addMessage(message: Message) {
        value.add(message)
        _messages.value = value.toList()
    }

    fun addPhotoMessages(uris: List<Uri>) {
        uris.forEach { uri ->
            val message = Message("1", "", "", uri, "1", "1", MessageType.PHOTO)
            addMessage(message)
        }
    }

    fun stopSocket() {
//        socketRepository.stopListening()
    }

    fun fetchMessage(arguments: Bundle?) {
        roomId = arguments?.getString(ROOM_ID)
    }
}