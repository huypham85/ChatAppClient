package com.vn.chat_app_client.presentation.settings

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vn.chat_app_client.data.api.auth.response.profile.ProfileResponse
import com.vn.chat_app_client.data.api.auth.response.profile.UpdateAvatarRequest
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
    val avatar: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val contentResolver: ContentResolver,
) : ViewModel() {
    private lateinit var profileResponse: ProfileResponse

    init {
        viewModelScope.launch {
            profileRepository.getProfile().fold(
                onSuccess = { response ->
                    profileResponse = response
                    _uiState.update {
                        it.copy(
                            fullName = "${response.firstName} ${response.lastName}",
                            username = response.username,
                            avatar = response.avatar
                        )
                    }
                }, onFailure = {

                }
            )
        }
    }

    sealed class Event {
        object NavigateToNewGroup : Event()
        object NavigateToLogin : Event()
    }

    private val _event = Channel<Event>(Channel.UNLIMITED)
    val event = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    fun navigateToNewGroup() {
        _event.trySend(Event.NavigateToNewGroup)
    }

    private val _uriLiveData = MutableLiveData<String>()
    val uriLiveData: LiveData<String>
        get() = _uriLiveData

    fun logOut() {
        _event.trySend(Event.NavigateToLogin)
    }

    fun updateAvatar(imgUri: Uri?) {
        imgUri?.let {
            val mimeTypeMap = MimeTypeMap.getSingleton()
            val fileEx = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(it))
            val storageReference = Firebase.storage.reference.child(
                System.currentTimeMillis().toString() + "." + fileEx
            )
            storageReference.putFile(it).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { it1 ->
                    updateAvatarRequest(it1.toString())
                }
            }.addOnFailureListener {
                Log.d(TAG, it.localizedMessage)
            }
        }
    }

    private fun updateAvatarRequest(avatar: String?) {
        avatar?.let {
            viewModelScope.launch {
                profileRepository.updateAvatar(
                    profileResponse.userId,
                    getProfileForUpdateAvatar(it)
                ).fold(
                    onSuccess = {
                        it.avatar?.let { avatar ->
                            _uriLiveData.postValue(avatar)
                        }
                    }, onFailure = {
                        Log.e(TAG, it.localizedMessage)
                    }
                )
            }
        }
    }

    private fun getProfileForUpdateAvatar(avatar: String): UpdateAvatarRequest {
        return UpdateAvatarRequest(
            profileResponse.userId,
            avatar,
            profileResponse.firstName,
            profileResponse.lastName,
            profileResponse.username
        )
    }
}
