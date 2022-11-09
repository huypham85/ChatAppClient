package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse
import com.vn.chat_app_client.data.api.room.RoomMessagesResponse
import com.vn.chat_app_client.data.model.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomService {

    @GET("/rooms")
    suspend fun getRooms(
    ): List<Room>


    @POST("/rooms")
    suspend fun createRoom(
        @Body createRoomRequest: CreateRoomRequest
    ): CreateRoomResponse

    @GET("/rooms/{roomId}")
    suspend fun getMessageByRoomId(
        @Path("roomId") roomId: String
    ): RoomMessagesResponse
}