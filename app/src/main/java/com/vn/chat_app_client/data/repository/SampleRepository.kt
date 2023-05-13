package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.database.LocalMessageDAO
import com.vn.chat_app_client.data.model.SampleModel
import javax.inject.Inject

class SampleRepository @Inject constructor(private val localMessageDAO: LocalMessageDAO) {
    suspend fun getAllData(): List<SampleModel> {
        return localMessageDAO.getAllData()
    }

    suspend fun insertData(sampleModel: SampleModel) {
        localMessageDAO.insertData(sampleModel)
    }
}