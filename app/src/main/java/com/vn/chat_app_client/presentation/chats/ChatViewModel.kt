package com.vn.chat_app_client.presentation.chats

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.MessageType
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.presentation.home.HomeFragment.Companion.RECEIVER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketRepository: SocketRepositoryImpl,
    messageRepository: MessageRepository,
    private val roomRepository: RoomRepository,
    private val savedAccountManager: SavedAccountManager,
) : ViewModel() {
    val messageResponse = messageRepository.newMessageReceive

    init {
        socketRepository.startListening()
    }

    var messageText = MutableStateFlow("")
    private val value = mutableListOf<Message>()
    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages = _messages.asStateFlow()

    fun sendNewMessage(message: Message) {
        if (message.text.isNotBlank()) {
            socketRepository.sendMessage(
                MessageSocketRequest(
                    roomId = message.roomId,
                    text = message.text
                )
            )
        }
        messageText.value = ""
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
        socketRepository.stopListening()
    }

    fun fetchMessage(arguments: Bundle?) {
        val receiverId = arguments?.getString(RECEIVER_ID)
        val members = listOf(savedAccountManager.fetchUserId() ?: "", receiverId ?: "")
        viewModelScope.launch {
            roomRepository.createRoom(CreateRoomRequest(members)).fold(
                onSuccess = {

                }, onFailure = {

                }
            )
        }
    }
}