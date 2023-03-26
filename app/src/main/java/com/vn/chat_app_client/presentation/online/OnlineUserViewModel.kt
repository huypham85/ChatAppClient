package com.vn.chat_app_client.presentation.online

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnlineUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    sealed class Event {
        class NavigateToChat(val roomId: String) : Event()
    }

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()


    private val _listUserShow = MutableStateFlow<List<User>>(listOf())
    val listUserShow: StateFlow<List<User>> = _listUserShow

    init {

    }

    fun getOnlineUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.listUsers()
                .fold(onSuccess = {
                    val itemsToShow = (1..4).random()
                    val onlineUsers = it.shuffled().take(itemsToShow)
                    _listUserShow.value = onlineUsers
                }, onFailure = {
                    Log.d(ContentValues.TAG, it.stackTraceToString())
                })
        }
    }
}