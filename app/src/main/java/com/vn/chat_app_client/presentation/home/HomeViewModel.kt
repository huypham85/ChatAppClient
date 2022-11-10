package com.vn.chat_app_client.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.model.ReceiveMessage
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    var modeUser: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: MessageRepository,
    private val userRepositoryImpl: UserRepository,
    private val roomRepositoryImpl: RoomRepository,
    private val savedAccountManager: SavedAccountManager,
) : ViewModel() {

    sealed class Event {
        class NavigateToChat(val roomId: String) : Event()
    }

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    val messageReceivedFlow: SharedFlow<ReceiveMessage> =
        repository.newMessageReceive.asSharedFlow()
    val idRoomReceive: SharedFlow<String> = repository.idRoomReceive.asSharedFlow()
    val receiveText: SharedFlow<String> = repository.receiveText.asSharedFlow()
    var listUser: List<User> = mutableListOf()
    var listRoom: List<Room> = mutableListOf()

    private val _listUserShow = MutableStateFlow<List<User>>(listOf())
    val listUserShow: StateFlow<List<User>> = _listUserShow

    private val _listRoomShow = MutableStateFlow<List<Room>>(listOf())
    val listRoomShow: StateFlow<List<Room>> = _listRoomShow

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepositoryImpl.listRooms()
                .fold(onSuccess = { it ->
                    listRoom = it
                    listRoom.sortedBy { it ->

                        it.lastMessage?.let { it1 ->
                            it1.createdAt
                        }
                    }
                    _listRoomShow.value = listRoom
                }, onFailure = {
                })
        }

        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryImpl.listUsers()
                .fold(onSuccess = {
                    listUser = it

                }, onFailure = {
                })
        }
    }

    fun searchUser(text: CharSequence) {
        try {
            val list = listUser.filter {
                it.username.lowercase().contains(text.toString().lowercase())
            }
            _listUserShow.value = list
            _uiState.value = HomeUiState(false, modeUser = true)
        } catch (e: Exception) {
            print(e)
        }
    }

    fun cancelSearchUser() {
        _listUserShow.value = listOf()
        _uiState.value = HomeUiState(false, modeUser = false)

    }

    fun createRoom(receiverId: String) {
        val members = listOf(savedAccountManager.fetchUserId() ?: "", receiverId)
        viewModelScope.launch {
            roomRepositoryImpl.createRoom(CreateRoomRequest(members)).fold(
                onSuccess = {
                    _event.trySend(Event.NavigateToChat(it.id))
                }, onFailure = {

                }
            )
        }
    }

    fun navToChat(idRoom: String) {
        _event.trySend(Event.NavigateToChat(idRoom))
    }

}