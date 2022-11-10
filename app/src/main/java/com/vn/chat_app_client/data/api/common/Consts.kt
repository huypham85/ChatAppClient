package com.vn.chat_app_client.data.api.common

object Consts {
    // todo: save in build config
    const val BASE_URL = "http://192.168.1.14:1111"
    const val AUTHORIZATION_KEY = "Authorization"
    const val JWT_PREFIX = "Bearer "
    const val SOCKET_URL = "http://192.168.1.14:80/messages"

    const val TIME_SERVER_PATTERN ="yyyy-MM-dd'T'HH:mm:ss"
    const val TIME_DAY_PATTERN ="dd-MM-yyyy"
    const val HOUR_PATTERN ="HH:mm"
    const val HOUR_DAY_PATTERN ="HH:mm,dd-MM-yyyy"
}