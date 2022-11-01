package com.vn.chat_app_client.presentation.home

import androidx.lifecycle.ViewModel
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
)
@HiltViewModel
class HomeViewModel@Inject constructor(
    val repository: MessageRepository,
    private val socketRepositoryImpl: SocketRepositoryImpl
): ViewModel() {

    init {
        socketRepositoryImpl.startListening()
    }

    val messageReceivedFlow: SharedFlow<Message> = repository.newMessageReceive.asSharedFlow()
    val idRoomReceive: SharedFlow<String> = repository.idRoomReceive.asSharedFlow()
    val receiveText: SharedFlow<String> = repository.receiveText.asSharedFlow()

//    private val listRoom = mutableListOf<Room>()
//    private val _rooms = MutableStateFlow(listRoom)
//    val rooms = _rooms.asStateFlow()

    fun sendCount(){
        socketRepositoryImpl.sendCount()
    }

    fun sendMessage(){
        socketRepositoryImpl.sendMessage(Message("1","Minh","1","6348e9191fe1036c662baecc","635d5ab6a9639ea8dfc93c54"))
    }








}