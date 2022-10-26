package com.vn.chat_app_client.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.api.auth.response.RegisterRequest
import com.vn.chat_app_client.data.api.auth.response.RegisterResponse
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    sealed class Event {
        class NavigateToLogin(val registerData: RegisterResponse) : Event()
        class Error(val message: String) : Event()
    }

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    val usernameInput = MutableStateFlow("")
    val passwordInput = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")

    fun registerAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.register(
                RegisterRequest(
                    usernameInput.value,
                    passwordInput.value,
                    firstName.value,
                    lastName.value
                )
            )
                .fold(onSuccess = {
                    _event.trySend(Event.NavigateToLogin(it))
                }, onFailure = {
                    _event.trySend(Event.Error("Username was existed"))
                })
        }
    }

}