package com.vn.chat_app_client.presentation.home

import androidx.lifecycle.ViewModel
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
)
@HiltViewModel
class HomeViewModel@Inject constructor(
    val repository: MessageRepository,
    private val socketRepositoryImpl: SocketRepositoryImpl
): ViewModel() {

    sealed class Event {
        object NavigateToHome : Event()
    }

    init {
        socketRepositoryImpl.startListening()
    }

    val messageReceivedFlow: SharedFlow<Message> = repository.newMessageReceive.asSharedFlow()
    val receiveText: SharedFlow<String> = repository.receiveText.asSharedFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    val usernameInput = MutableStateFlow("")
    val passwordInput = MutableStateFlow("")

    fun sendCount(){
        socketRepositoryImpl.sendCount()
    }

    fun sendMessage(){
        socketRepositoryImpl.sendMessage(Message("1","Minh","1","1","1"))
    }








}