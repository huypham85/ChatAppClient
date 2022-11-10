package com.vn.chat_app_client.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.domain.repository.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val fullName: String = "",
    val username: String = "",
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            profileRepository.getProfile().fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            fullName = "${response.firstName} ${response.lastName}",
                            username = response.username
                        )
                    }
                }, onFailure = {

                }
            )
        }

    }

    sealed class Event {
        object NavigateToNewGroup : Event()
        object NavigateToLogin: Event()
    }

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    fun navigateToNewGroup() {
        _event.trySend(Event.NavigateToNewGroup)
    }

    fun logOut() {
        _event.trySend(Event.NavigateToLogin)
    }


}