package com.vn.chat_app_client.data.api.common

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommonResponse<D>(
    @SerializedName("statusCode")
    val statusCode: Int,
    val message: String,
    val data: D
): Serializable
