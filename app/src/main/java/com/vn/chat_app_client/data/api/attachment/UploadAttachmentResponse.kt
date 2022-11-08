package com.vn.chat_app_client.data.api.attachment

data class UploadAttachmentResponse(
    val id: String,
    val filename: String,
    val mimetype: String,
    val size: Long,
)
