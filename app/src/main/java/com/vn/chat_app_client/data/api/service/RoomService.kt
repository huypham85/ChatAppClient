package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse
import com.vn.chat_app_client.data.api.room.RoomMessagesResponse
import com.vn.chat_app_client.data.model.Room
import retrofit2.http.*

interface RoomService {

    @GET("/rooms")
    suspend fun getRooms(@Query("min_member") mimMember: Int): List<Room>


    @POST("/rooms")
    suspend fun createRoom(
        @Body createRoomRequest: CreateRoomRequest
    ): CreateRoomResponse

    @GET("/rooms/{roomId}")
    suspend fun getMessageByRoomId(
        @Path("roomId") roomId: String
    ): RoomMessagesResponse
}