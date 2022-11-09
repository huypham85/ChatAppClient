package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse
import com.vn.chat_app_client.data.api.room.RoomMessagesResponse
import com.vn.chat_app_client.data.model.Room

interface RoomRepository {
    suspend fun createRoom(createRoomRequest: CreateRoomRequest): Result<CreateRoomResponse>
    suspend fun listRooms(): Result<List<Room>>
    suspend fun getMessageByRoomId(roomId: String): Result<RoomMessagesResponse>
}