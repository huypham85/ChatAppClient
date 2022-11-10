package com.vn.chat_app_client.data.api.attachment

import com.google.gson.annotations.SerializedName

data class UploadAttachmentResponse(
    @SerializedName("_id")
    val id: String,
    val filename: String,
    val mimetype: String,
    @SerializedName("uploadedAt")
    val uploadedAt: String,
    val size : Long,
)
