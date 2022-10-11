package com.vn.chat_app_client.data.api.response

sealed class ResultDataResponse<T> constructor(val resultCode: Int) {
    class ResultDataEmpty<T> : ResultDataResponse<T>(-1)
    data class ResultDataSuccess<T>(val code: Int, val body: T) : ResultDataResponse<T>(code)
}