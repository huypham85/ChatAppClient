package com.vn.chat_app_client.domain.repository.repository

import android.util.Log
import com.vn.chat_app_client.data.api.service.AttachmentService
import com.vn.chat_app_client.data.model.ReceiveMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface MessageRepository {
    suspend fun receiveNewText(text: String)
    suspend fun receiveNewMessage(message: ReceiveMessage)
    suspend fun sendAttachment(attachmentPaths: List<String?>): Result<String>
    val newMessageReceive: MutableSharedFlow<ReceiveMessage>

    val receiveText: MutableSharedFlow<String>

    val idRoomReceive: MutableSharedFlow<String>
}

@Singleton
class MessageRepositoryImpl @Inject constructor(
    val service: AttachmentService,
) : MessageRepository {

    private var _newMessageReceive = MutableSharedFlow<ReceiveMessage>()
    override val newMessageReceive: MutableSharedFlow<ReceiveMessage>
        get() = _newMessageReceive

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
        _idRoomReceive.emit(message.roomId)
    }

    override suspend fun sendAttachment(attachmentPaths: List<String?>): Result<String> {
        attachmentPaths.forEach { path ->
            path?.let {
                val file = File(path)
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                Log.d("BODY PHOTO", body.toString())
                return try {
                    val response = service.uploadAttachment(body)
                    Result.success(response.filename)
                } catch (ex: Exception) {
                    println(ex)
                    Result.failure(ex)
                }

            }

        }
        return Result.success("")
    }

}