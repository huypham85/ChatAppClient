package com.vn.chat_app_client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.chat_app_client.data.model.SampleModel
import com.vn.chat_app_client.data.repository.SampleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(private val sampleRepository: SampleRepository) :
    ViewModel() {
    private val _messageLiveData = MutableLiveData<List<SampleModel>>()
    val messageLiveData: LiveData<List<SampleModel>>
        get() = _messageLiveData

    private fun getAllMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            _messageLiveData.postValue(sampleRepository.getAllData())
        }
    }

    fun insertMessage(sampleModel: SampleModel){
        viewModelScope.launch (Dispatchers.IO){
            sampleRepository.insertData(sampleModel)
            getAllMessage()
        }
    }
}