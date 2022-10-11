package com.vn.chat_app_client.data.api

import com.vn.chat_app_client.data.api.response.loginresponse.LoginResponse
import com.vn.chat_app_client.ui.utils.Constants.LOGIN_URL
import retrofit2.http.*

interface AuthService {

    @POST(LOGIN_URL)
    suspend fun checkLogin(
        @Body user: Map<String, String>
    ): LoginResponse
}