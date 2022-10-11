package com.vn.chat_app_client.ui.auth

import androidx.lifecycle.*
import com.vn.chat_app_client.data.api.response.DataResponse
import com.vn.chat_app_client.data.api.response.LoadingStatus
import com.vn.chat_app_client.data.api.response.loginresponse.LoginResponse
import com.vn.chat_app_client.data.model.User
import com.vn.chat_app_client.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: AuthRepository
): ViewModel() {
    var loginResult = MutableLiveData<DataResponse<LoginResponse>>(DataResponse.DataIdle())
    // TODO: add loading indicator
    val isLoading: LiveData<Boolean> = Transformations.map(loginResult) {
        loginResult.value?.loadingStatus == LoadingStatus.Loading
    }

    fun checkLogin(user: User) {
        if (loginResult.value?.loadingStatus != LoadingStatus.Loading) {
            loginResult.value?.loadingStatus = LoadingStatus.Loading

            viewModelScope.launch(Dispatchers.IO) {
                val res = userRepository.checkLogin(user)
                if (res != null) {
                    loginResult.postValue(DataResponse.DataSuccess(res))
                } else {
                    loginResult.postValue(DataResponse.DataError())
                }
            }
        }
    }
}