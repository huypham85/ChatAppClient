package com.vn.chat_app_client.data.repository

import android.util.Log
import com.google.gson.Gson
import com.vn.chat_app_client.data.api.common.Consts
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.message.MessageSocketRequest
import com.vn.chat_app_client.data.model.ReceiveMessage
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import io.socket.client.IO
import io.socket.client.Manager.EVENT_RECONNECT
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONException
import java.net.URISyntaxException
import java.security.Provider.Service
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketRepositoryImpl @Inject constructor(
    private val repository: MessageRepository,
    private val savedAccountManager: SavedAccountManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    lateinit var mSocket: Socket

    private val TAG = this.javaClass.simpleName
    private val EVENT_CONNECT: String = Socket.EVENT_CONNECT
    private val EVENT_CONNECT_ERROR: String = Socket.EVENT_CONNECT_ERROR
    private val EVENT_DISCONNECT: String = Socket.EVENT_DISCONNECT
    private val EVENT_NEW_MESSAGE = "sent"
    private val EVENT_SEND_MESSAGE = "send"
    private val EVENT_RECEIVED_MESSAGE = "message"


    private val connectListener = Emitter.Listener { args ->
        Log.d(TAG, "onConnect ...")
    }

    private val reconnectListener = Emitter.Listener { args ->
        Log.d(TAG, "onReconnect ...")
    }

    private val connectionErrorListener = Emitter.Listener { args ->
        Log.d(TAG, "onConnectionError $args")
    }

    private val disconnectListener = Emitter.Listener { args ->
        Log.d(TAG, "onDisconnect ...")
    }

    // Thực hiện khi nhận tin nhắn mới
    private val receivedMessageListener = Emitter.Listener { args ->
        val rawMessage = args[0].toString()
        Log.d(TAG, "onNewMessage: $rawMessage")
        try {
            val receiveMessage = Gson().fromJson(rawMessage, ReceiveMessage::class.java)
            scope.launch {
                try {
                    repository.receiveNewMessage(receiveMessage)
                } catch (e: Exception) {

                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    private val receiveText = Emitter.Listener { args ->
        val rawMessage = args[0].toString()
        Log.d(TAG, "onNewMessage: $rawMessage")
        try {
            scope.launch {
                repository.receiveNewText(rawMessage)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    // Bắt đầu lắng nghe theo event
    @Throws(URISyntaxException::class)
    fun startListening(): Result<Unit> {
        return try {
            val options = IO.Options()
            options.extraHeaders = mutableMapOf<String, MutableList<String>>(
                "Authorization" to Arrays.asList("Bearer ${savedAccountManager.fetchAuthToken()}")
            )
            mSocket = IO.socket(Consts.SOCKET_URL, options)
            mSocket.on(EVENT_CONNECT, connectListener)
            mSocket.on(EVENT_DISCONNECT, disconnectListener)
            mSocket.on(EVENT_RECONNECT, reconnectListener)
            mSocket.on(EVENT_CONNECT_ERROR, connectionErrorListener)
            mSocket.on(EVENT_RECEIVED_MESSAGE, receivedMessageListener)
            mSocket.on("counter", receiveText)
            mSocket.connect()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun stopListening() {
        mSocket.disconnect()
    }

    fun sendMessage(message: MessageSocketRequest) {
        mSocket.emit(EVENT_SEND_MESSAGE, Gson().toJson(message))
        Log.d("json message to server", Gson().toJson(message))
    }

    fun sendCount() {
        mSocket.emit("counter", 1)
    }


}