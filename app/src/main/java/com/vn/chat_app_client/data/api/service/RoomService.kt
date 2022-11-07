package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.room.CreateRoomRequest
import com.vn.chat_app_client.data.api.room.CreateRoomResponse
import com.vn.chat_app_client.data.model.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoomService {

    @GET("/rooms")
    suspend fun getRooms(
    ):List<Room>


    @POST("/rooms")
    suspend fun createRoom(
        @Body createRoomRequest: CreateRoomRequest
    ): CreateRoomResponse
}