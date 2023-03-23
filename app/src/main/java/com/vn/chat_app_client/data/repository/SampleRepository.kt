package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.database.LocalMessageDAO
import com.vn.chat_app_client.data.model.SampleModel
import javax.inject.Inject

class SampleRepository @Inject constructor(private val localMessageDAO: LocalMessageDAO) {
    suspend fun getAllMessage():List<SampleModel>{
        return localMessageDAO.getAllMessage()
    }

    suspend fun insertMessage(sampleModel: SampleModel){
        localMessageDAO.insertMessage(sampleModel)
    }
}