package com.vn.chat_app_client.presentation.chats

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.model.MessageType
import com.vn.chat_app_client.data.model.ReceiveMessage
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.presentation.home.HomeFragment.Companion.ROOM_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketRepository: SocketRepositoryImpl,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository,
    private val savedAccountManager: SavedAccountManager,
) : ViewModel() {
    val messageResponse = messageRepository.newMessageReceive
    private var roomId: String? = ""

    var messageText = MutableStateFlow("")
    private val value = mutableListOf<ReceiveMessage>()
    private val _messages = MutableStateFlow(emptyList<ReceiveMessage>())
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

    fun addMessage(message: ReceiveMessage) {
        if (message.attachments.isEmpty()) message.type = MessageType.TEXT
        else message.type = MessageType.PHOTO

        if (message.roomId == this.roomId) {
            value.add(message)
            _messages.value = value.toList()
        } else {
            Log.d("ChatVM", "message from other box: ${message.text}")
        }
    }

    fun addPhotoMessages(paths: List<String?>) {
        viewModelScope.launch {
            messageRepository.sendAttachment(paths)
        }
    }

    fun fetchMessage(arguments: Bundle?) {
        roomId = arguments?.getString(ROOM_ID)
    }
}