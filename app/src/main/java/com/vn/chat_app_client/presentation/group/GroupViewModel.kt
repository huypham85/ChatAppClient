package com.vn.chat_app_client.presentation.group

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.model.ReceiveMessage
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.ProfileRepository
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import com.vn.chat_app_client.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupUiState(
    val isLoading: Boolean = false,
    var modeUser: Boolean = false,
    var imgAvt: String? = null
)

@HiltViewModel
class GroupViewModel @Inject constructor(
    val repository: MessageRepository,
    private val userRepositoryImpl: UserRepository,
    private val roomRepositoryImpl: RoomRepository,
    private val profileRepository: ProfileRepository,
    private val savedAccountManager: SavedAccountManager,
) :
    ViewModel() {

    sealed class Event {
        class NavigateToChat(val roomId: String) : Event()
        object NavigateToNewGroup : Event()

    }

    private lateinit var profileResponse: ProfileResponse

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()


    private var listRoom: List<Room> = mutableListOf()
    private val _listRoomShow = MutableStateFlow<List<Room>>(listOf())
    val listRoomShow: StateFlow<List<Room>>
        get() = _listRoomShow

    var listUser: List<User> = mutableListOf()
    private val _listUserShow = MutableStateFlow<List<User>>(listOf())
    val listUserShow: StateFlow<List<User>> = _listUserShow


    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState
    val messageReceivedFlow: SharedFlow<ReceiveMessage> = repository.newMessageReceiveToHome

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryImpl.listUsers()
                .fold(onSuccess = {
                    listUser = it
                }, onFailure = {
                    Log.d(ContentValues.TAG, it.stackTraceToString())
                })
            profileRepository.getProfile().fold(onSuccess = { response ->
                profileResponse = response
                _uiState.update {
                    it.copy(imgAvt = profileResponse.avatar)
                }
            }, onFailure = {

            })
        }
        getData()
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepositoryImpl.listRooms(
                2
            ).fold(
                onSuccess = {
                    listRoom = it
                    _listRoomShow.value = listRoom
                }, onFailure = {
                    Log.d(ContentValues.TAG, it.stackTraceToString())
                }
            )
        }
    }

    fun searchUser(text: CharSequence) {
        try {
            val list = listUser.filter {
                it.username.lowercase().contains(text.toString().lowercase())
            }
            _listUserShow.value = list
            _uiState.value = GroupUiState(false, modeUser = true)
        } catch (e: Exception) {
            print(e)
        }
    }

    fun cancelSearchUser() {
        _listRoomShow.value = listOf()
        _uiState.value = GroupUiState(false, modeUser = false)
    }

    fun createRoom(receiverId: String) {
        val members = listOf(savedAccountManager.fetchUserId() ?: "", receiverId)
        viewModelScope.launch {
            roomRepositoryImpl.createRoom(CreateRoomRequest(members)).fold(
                onSuccess = {
                    _event.trySend(Event.NavigateToChat(it.id))
                }, onFailure = {
                    Log.d(ContentValues.TAG, it.stackTraceToString())
                }
            )
        }
    }

    fun navToChat(idRoom: String) {
        _event.trySend(Event.NavigateToChat(idRoom))
    }

    fun navigateToNewGroup() {
        _event.trySend(Event.NavigateToNewGroup)
    }
}