package com.vn.chat_app_client.data.repository

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse
import com.vn.chat_app_client.data.api.room.RoomMessagesResponse
import com.vn.chat_app_client.data.api.service.RoomService
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.domain.repository.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val service: RoomService,
) : RoomRepository {
    override suspend fun createRoom(createRoomRequest: CreateRoomRequest): Result<CreateRoomResponse> {
        return try {
            val response = service.createRoom(createRoomRequest)
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun listRooms(): Result<List<Room>> {
        return try {
            val response = service.getRooms()
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun getMessageByRoomId(roomId: String): Result<RoomMessagesResponse> {
        return try {
            val response = service.getMessageByRoomId(roomId)
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}