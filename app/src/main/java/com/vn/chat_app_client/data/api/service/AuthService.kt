package com.vn.chat_app_client.data.api.service

import com.vn.chat_app_client.data.api.auth.response.LoginRequest
import com.vn.chat_app_client.data.api.auth.response.LoginResponse
import com.vn.chat_app_client.data.api.common.CommonResponse
import com.vn.chat_app_client.data.api.common.Consts.LOGIN_URL
import retrofit2.http.*

interface AuthService {

    @POST(LOGIN_URL)
    suspend fun checkLogin(
        @Body user: LoginRequest
    ): CommonResponse<LoginResponse>
}