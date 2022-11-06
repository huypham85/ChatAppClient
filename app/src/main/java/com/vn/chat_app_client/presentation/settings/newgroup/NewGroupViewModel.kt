package com.vn.chat_app_client.presentation.settings.newgroup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import com.vn.chat_app_client.domain.repository.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewGroupViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepository,
    private val roomRepositoryImpl: RoomRepository,
) : ViewModel() {


    var listChooseUser = mutableListOf<User>()
    private val _listChooseUserShow = MutableStateFlow<List<User>>(listOf())
    val listChooseUserShow: StateFlow<List<User>> = _listChooseUserShow


    var listUser: List<User> = mutableListOf()
    private val _listUserShow = MutableStateFlow<List<User>>(listOf())
    val listUserShow: StateFlow<List<User>> = _listUserShow

    init {
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
        } catch (e: Exception) {
            print(e)
        }
    }

    fun cancelSearchUser() {
        _listUserShow.value = listUser
    }

    fun chooseUser(userId: String) {
        val user = listUserShow.value.find { it.id == userId }
        user?.let {
            if (listChooseUser.find { it.id == userId } == null) {
                listChooseUser.add(user)
                _listChooseUserShow.value = listChooseUser.toList()
            }
        }
    }

    fun unChooseUser(userId: String) {
        listChooseUser = listChooseUser.filter { it.id != userId }.toMutableList()
        _listChooseUserShow.value = listChooseUser.toList()

    }

    fun addNewGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<String>()
            for (user in _listChooseUserShow.value) {
                list.add(user.id)
            }
            val createRoomRequest = CreateRoomRequest(list)
            roomRepositoryImpl.createRoom(createRoomRequest)
                .fold(onSuccess = {
                    Log.e(this.javaClass.simpleName, "Success")
                }, onFailure = {
                })
        }
    }
}