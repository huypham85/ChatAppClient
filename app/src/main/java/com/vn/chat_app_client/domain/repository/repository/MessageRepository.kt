package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.attachment.UploadAttachmentResponse
import com.vn.chat_app_client.data.api.service.AttachmentService
import com.vn.chat_app_client.data.model.ReceiveMessage
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface MessageRepository {
    suspend fun receiveNewText(text: String)
    suspend fun receiveNewMessage(message: ReceiveMessage)
    suspend fun clearMessageCache()
    suspend fun sendAttachment(attachmentPath: String?): Result<UploadAttachmentResponse>

    val newMessageReceive: StateFlow<ReceiveMessage?>
    val newMessageReceiveToHome: SharedFlow<ReceiveMessage>
    val receiveText: MutableSharedFlow<String>

    val idRoomReceive: MutableSharedFlow<String>
}

@Singleton
class MessageRepositoryImpl @Inject constructor(
    val service: AttachmentService,
) : MessageRepository {

    private val _newMessageReceive = MutableStateFlow<ReceiveMessage?>(null)
    override val newMessageReceive: StateFlow<ReceiveMessage?>
        get() {
            return _newMessageReceive.asStateFlow()
        }

    private var _newMessageReceiveToHome = MutableSharedFlow<ReceiveMessage>(
        replay = Int.MAX_VALUE,
        extraBufferCapacity = Int.MAX_VALUE
    )
    override val newMessageReceiveToHome: SharedFlow<ReceiveMessage> =
        _newMessageReceiveToHome.asSharedFlow()
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

    override suspend fun clearMessageCache() {
        _newMessageReceive.emit(null)
    }

    override suspend fun sendAttachment(attachmentPath: String?): Result<UploadAttachmentResponse> {
        return try {
            val file = File(attachmentPath ?: "")
            val requestFile: RequestBody =
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = service.uploadAttachment(body)
            Result.success(response)
        } catch (ex: Exception) {
            println(ex)
            Result.failure(ex)
        }
    }
}