package com.vn.chat_app_client.presentation

import androidx.lifecycle.ViewModel
import com.vn.chat_app_client.data.repository.SocketRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketRepository: SocketRepositoryImpl,
) : ViewModel() {
    fun stopSocket() {
        socketRepository.stopListening()
    }

    fun startSocket() {
        socketRepository.startListening()
    }
}