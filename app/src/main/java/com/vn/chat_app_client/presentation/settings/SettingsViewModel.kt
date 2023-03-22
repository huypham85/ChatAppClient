package com.vn.chat_app_client.presentation.settings

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.vn.chat_app_client.domain.repository.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
        object NavigateToLogin : Event()
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

    fun uploadImage(context: Context, imgUri: Uri?): String {
        var uri: String = ""
        imgUri?.let {
            val storageReference: StorageReference = Firebase.storage.reference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(context, it)
            )
            storageReference.putFile(it).addOnSuccessListener {
                OnCompleteListener<UploadTask>
                {
                    storageReference.downloadUrl.addOnSuccessListener { it1 ->
                        uri = it1.toString()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context,"upload Fail", Toast.LENGTH_SHORT).show()
            }
        }
        return uri
    }

    private fun getFileExtension(context: Context, imgUri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgUri))
    }


}