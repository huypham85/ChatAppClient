package com.vn.chat_app_client.presentation.settings

import androidx.lifecycle.ViewModel
import com.vn.chat_app_client.domain.repository.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val profileRepository:  ProfileRepository,
) : ViewModel() {

    sealed class Event {
        object NavigateToNewGroup : Event()
    }

    private val _event = Channel<SettingsViewModel.Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()



    fun navigateToNewGroup() {
        _event.trySend(SettingsViewModel.Event.NavigateToNewGroup)
    }





}