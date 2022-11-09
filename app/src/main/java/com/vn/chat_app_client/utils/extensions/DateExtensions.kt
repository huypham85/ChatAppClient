package com.vn.chat_app_client.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateView(format: String): String {
    return try {
        SimpleDateFormat(format).format(this)
    } catch (e: Exception) {
        SimpleDateFormat(format).format(Calendar.getInstance().time)
    }
}
