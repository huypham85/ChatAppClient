package com.vn.chat_app_client.data.api.service

import retrofit2.http.Multipart
import retrofit2.http.POST

interface MessageService {
    @Multipart
    @POST("/attachments/upload")
    suspend fun uploadPhoto(

    )
}