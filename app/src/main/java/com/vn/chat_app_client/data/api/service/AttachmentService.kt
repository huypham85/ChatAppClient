package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.attachment.UploadAttachmentResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface AttachmentService {
    @Multipart
    @POST("/attachments/upload")
    suspend fun uploadAttachment(
        @Part attachment: MultipartBody.Part?,
    ): UploadAttachmentResponse
}