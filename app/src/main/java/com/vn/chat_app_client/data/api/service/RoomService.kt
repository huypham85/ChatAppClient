package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.data.model.User
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RoomService {

    @GET("/rooms")
    suspend fun getRooms(
        @Query("size") size: Int,
        @Query("page") page: Int,
    )

//    @GET("/users")
//    suspend fun getRoom(
//        @Query("id") id: String,
//    ): List<User>

    @POST("/rooms")
    suspend fun createRoom(): Room
}