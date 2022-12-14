package com.vn.chat_app_client.domain.repository.repository

import android.util.Log
import com.vn.chat_app_client.data.api.attachment.UploadAttachmentResponse
import com.vn.chat_app_client.data.api.service.AttachmentService
import com.vn.chat_app_client.data.model.ReceiveMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface MessageRepository {
    suspend fun receiveNewText(text: String)
    suspend fun receiveNewMessage(message: ReceiveMessage)
    suspend fun sendAttachment(attachmentPath: String?): Result<UploadAttachmentResponse>
    val newMessageReceive: SharedFlow<ReceiveMessage>
    val newMessageReceiveToHome: SharedFlow<ReceiveMessage>
    val receiveText: MutableSharedFlow<String>

    val idRoomReceive: MutableSharedFlow<String>
}

@Singleton
class MessageRepositoryImpl @Inject constructor(
    val service: AttachmentService,
) : MessageRepository {

    private val _newMessageReceive = MutableSharedFlow<ReceiveMessage>(replay = Int.MAX_VALUE, extraBufferCapacity = Int.MAX_VALUE)
    override val newMessageReceive: SharedFlow<ReceiveMessage> = _newMessageReceive.asSharedFlow()

    private var _newMessageReceiveToHome = MutableSharedFlow<ReceiveMessage>(replay = Int.MAX_VALUE, extraBufferCapacity = Int.MAX_VALUE)
    override val newMessageReceiveToHome: SharedFlow<ReceiveMessage> = _newMessageReceiveToHome.asSharedFlow()
    private var _idRoomReceive = MutableSharedFlow<String>()
    override val idRoomReceive: MutableSharedFlow<String>
        get() = _idRoomReceive

    private var _receiveText = MutableSharedFlow<String>()
    override val receiveText: MutableSharedFlow<String>
        get() = _receiveText

    override suspend fun receiveNewText(text: String) {
        _receiveText.emit(text)
    }

    override suspend fun receiveNewMessage(message: ReceiveMessage) {
        _newMessageReceive.emit(message)
        _newMessageReceiveToHome.emit(message)
        _idRoomReceive.emit(message.roomId)
    }

    override suspend fun sendAttachment(attachmentPath: String?): Result<UploadAttachmentResponse> {
        return try {
            val file = File(attachmentPath ?: "")
            val requestFile: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = service.uploadAttachment(body)
            Result.success(response)
        } catch (ex: Exception) {
            println(ex)
            Result.failure(ex)
        }
    }
}