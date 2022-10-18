package com.vn.chat_app_client.presentation.auth

import androidx.lifecycle.*
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    sealed class Event {
        object NavigateToHome : Event()
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    val usernameInput = MutableStateFlow("")
    val passwordInput = MutableStateFlow("")

    fun checkLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkLogin(User(usernameInput.value, passwordInput.value)).fold(onSuccess = { loginResponse ->
                saveAccount(loginResponse)
            }, onFailure = {

            })
        }
    }

    private fun saveAccount(loginResponse: LoginResponse) {
        repository.saveAccount(loginResponse).fold(
            onSuccess = {
                _event.trySend(Event.NavigateToHome)
            }, onFailure = {

            }
        )
    }
}
