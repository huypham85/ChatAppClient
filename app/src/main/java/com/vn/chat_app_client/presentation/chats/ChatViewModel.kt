package com.vn.chat_app_client.presentation.chats

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.api.service.ProfileService
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.MessageType
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketRepository: SocketRepositoryImpl,
    messageRepository: MessageRepository,
) : ViewModel() {
    val messageResponse = messageRepository.newMessageReceive
    init {
        socketRepository.startListening()
//        viewModelScope.launch {
//            messageResponse.collect { message ->
//                _messages.value.add(message)
//            }
//        }
    }

    var messageText = MutableStateFlow("")
    private val _messages = MutableStateFlow(mutableListOf<Message>())
    val messages = _messages.asStateFlow()

    fun sendNewMessage(message: Message) {
        if (message.text.isNotBlank()) {
            socketRepository.sendMessage(MessageSocketRequest(roomId = message.roomId, text = message.text))
        }
//            _messages.value.add(message)
        messageText.value = ""
    }

    fun addMessage(message: Message) {
        _messages.value.add(message)
    }

    fun addPhotoMessages(uris: List<Uri>) {
        uris.forEach { uri ->
            val message = Message("1", "", "", uri, "1", "1", MessageType.PHOTO)
            _messages.value.add(message)
        }
    }
}