package com.vn.chat_app_client.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.service.ProfileService
import com.vn.chat_app_client.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val service: ProfileService,
) : ViewModel() {

    init {
        viewModelScope.launch {
            service.fetchProfile()
        }
    }

    private val listMessage = mutableListOf<Message>()

    var messageText = MutableStateFlow("")
    private val _messages = MutableStateFlow(listMessage)
    val messages = _messages.asStateFlow()

    fun addNewMessage(message: Message) {
        if (message.text.isNotBlank())
        _messages.value.add(message)
        messageText.value = ""
    }
}