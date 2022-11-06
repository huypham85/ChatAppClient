package com.vn.chat_app_client.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.model.Message
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    var modeUser:Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    messageRepositoryImpl: MessageRepository,
    private val socketRepositoryImpl: SocketRepositoryImpl,
    private val userRepositoryImpl: UserRepository
) : ViewModel() {

    val messageReceivedFlow: SharedFlow<Message> = messageRepositoryImpl.newMessageReceive.asSharedFlow()
    val idRoomReceive: SharedFlow<String> = messageRepositoryImpl.idRoomReceive.asSharedFlow()
    val receiveText: SharedFlow<String> = messageRepositoryImpl.receiveText.asSharedFlow()
    var listUser: List<User> = mutableListOf<User>()

    private val _listUserShow = MutableStateFlow<List<User>>(listOf())
    val listUserShow: StateFlow<List<User>> = _listUserShow

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState:StateFlow<HomeUiState> = _uiState

    init {
        socketRepositoryImpl.startListening()
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryImpl.listUsers()
                .fold(onSuccess = {
                    listUser = it
                }, onFailure = {
                })
        }
    }

    fun sendCount() {
        socketRepositoryImpl.sendCount()
    }

    fun sendMessage() {
        socketRepositoryImpl.sendMessage(
            Message(
                "1",
                "Minh",
                "1",
                "6348e9191fe1036c662baecc",
                "635d5ab6a9639ea8dfc93c54"
            )
        )
    }

    fun searchUser(text: CharSequence) {
        try {
            val list = listUser.filter {
                it.username.lowercase().contains(text.toString().lowercase())
            }
            _listUserShow.value = list
            _uiState.value = HomeUiState(false, modeUser = true)
        }catch (e:Exception){
            print(e)
        }
    }

    fun cancelSearchUser(){
        _listUserShow.value = listOf()
        _uiState.value = HomeUiState(false, modeUser = false)
    }


}