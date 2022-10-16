package com.vn.chat_app_client.presentation.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SplashViewModel : ViewModel() {

    sealed class Event {
        object NavigateToLogin : Event()
        object NavigateToHome : Event()
    }

    private val _event = Channel<Event>(capacity = Channel.UNLIMITED)
    val events = _event.receiveAsFlow()

    fun checkLogin() {
        // TODO: if logged in -> MainActivity else -> LoginActivity
        _event.trySend(Event.NavigateToLogin)
    }
}