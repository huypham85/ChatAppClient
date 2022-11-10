package com.vn.chat_app_client.presentation.chats

import android.content.ContentValues.TAG
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    val messageResponse: SharedFlow<ReceiveMessage> = messageRepository.newMessageReceive
    private var roomId: String? = ""

    var messageText = MutableStateFlow("")
    private val value = mutableListOf<RoomMessage>()
    private val _messages = MutableStateFlow(emptyList<RoomMessage>())
    val messages = _messages.asStateFlow()
    private val _roomName = MutableStateFlow("")
    val roomName = _roomName.asStateFlow()
    private val _enableSend = MutableStateFlow(false)
    val enableSend = _enableSend.asStateFlow()

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

    fun addNewMessage(message: ReceiveMessage) {
        val newMessage =
            RoomMessage(
                message.text,
                message.sender.id,
                message.createdAt,
                message.attachments,
                senderName = message.sender.username
            )
        Log.d("New Message", newMessage.toString())
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

    private fun addPreviousMessage(messages: List<RoomMessage>) {
        messages.forEach { message ->
            if (message.attachments.isEmpty()) message.type = MessageType.TEXT
            else message.type = MessageType.PHOTO
        }
        value.addAll(messages)
        Log.d("LIST OLD MESSAGES", value.map { it.type }.toString())
        _messages.value = value.toList()
    }

    fun addPhotoMessages(paths: List<String?>) {
        viewModelScope.launch {
            paths.forEach { path ->
                messageRepository.sendAttachment(path).fold(
                    onSuccess = {
                        socketRepository.sendMessage(
                            MessageSocketRequest(
                                roomId ?: "",
                                "image",
                                attachments = listOf(it.id)
                            )
                        )
                    }, onFailure = {
                        Log.d(TAG, it.stackTraceToString())
                    }
                )
            }
        }
    }

    fun fetchMessage(arguments: Bundle?) {
        roomId = arguments?.getString(ROOM_ID)
        viewModelScope.launch(Dispatchers.IO) {
            roomId?.let {
                roomRepository.getMessageByRoomId(it).fold(
                    onSuccess = { response ->
                        _roomName.emit(response.name)
                        val usernameMap = response.members.associate { adminId ->
                            adminId.id to adminId.username
                        }
                        val oldMessages = mutableListOf<RoomMessage>()
                        response.messages.forEach { message ->
                            message.senderName = usernameMap[message.senderId]
                            oldMessages.add(message)
                        }
                        addPreviousMessage(oldMessages)
                    }, onFailure = { exception ->
                        Log.d(TAG, exception.stackTraceToString())
                    }
                )
            }
        }

    }

    fun updateSendButton(enable: Boolean) {
        viewModelScope.launch {
            _enableSend.emit(enable)
        }
    }
}