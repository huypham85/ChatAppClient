package com.vn.chat_app_client.domain.repository.repository

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse

interface RoomRepository {
    suspend fun createRoom(createRoomRequest: CreateRoomRequest) : Result<CreateRoomResponse>
}