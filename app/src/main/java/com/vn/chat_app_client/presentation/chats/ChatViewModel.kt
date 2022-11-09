package com.vn.chat_app_client.presentation.chats

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.model.MessageType
import com.vn.chat_app_client.data.model.ReceiveMessage
import com.vn.chat_app_client.data.model.RoomMessage
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.presentation.home.HomeFragment.Companion.ROOM_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
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
    private val value = mutableListOf<RoomMessage>()
    private val _messages = MutableStateFlow(emptyList<RoomMessage>())
    val messages = _messages.asStateFlow()
    private val _roomName = MutableStateFlow("")
    val roomName = _roomName.asStateFlow()

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
        val newMessage =
            RoomMessage(
                message.text,
                message.sender.id,
                message.createdAt,
                message.attachments,
                senderName = message.sender.username
            )
        if (newMessage.attachments.isEmpty()) newMessage.type = MessageType.TEXT
        else newMessage.type = MessageType.PHOTO

        if (message.roomId == this.roomId) {
            value.add(newMessage)
            _messages.value = value.toList()
        } else {
            // TODO: push notification when message comes
            Log.d("ChatVM", "message from other box: ${message.text}")
        }
    }

    private fun addPreviousMessage(message: RoomMessage) {
        if (message.attachments.isEmpty()) message.type = MessageType.TEXT
        else message.type = MessageType.PHOTO

        value.add(message)
        _messages.value = value.toList()
    }

    fun addPhotoMessages(paths: List<String?>) {
        viewModelScope.launch {
            messageRepository.sendAttachment(paths).fold(
                onSuccess = {
                    val message = RoomMessage("",savedAccountManager.fetchUserId()?: "",Date().toString(),
                        listOf(it),MessageType.PHOTO)
                    value.add(message)
                    _messages.value = value.toList()
                }, onFailure = {

                }
            )
        }
    }

    fun fetchMessage(arguments: Bundle?) {
        roomId = arguments?.getString(ROOM_ID)
        viewModelScope.launch {
            roomId?.let {
                roomRepository.getMessageByRoomId(it).fold(
                    onSuccess = { response ->
                        _roomName.emit(response.name)
                        val usernameMap = response.members.associate { adminId ->
                            adminId.id to adminId.username
                        }
                        response.messages.forEach { message ->
                            message.senderName = usernameMap[message.senderId]
                            addPreviousMessage(message)
                        }
                    }, onFailure = {

                    }
                )
            }
        }

    }
}