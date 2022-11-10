package com.vn.chat_app_client.utils.extensions

import android.text.format.DateUtils
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

fun String.base64Decoded(): String? {
    return try {
        val bytes = Base64.decode(this, Base64.URL_SAFE)
        String(bytes, Charsets.UTF_8)
    } catch (e: Exception) {
        null
    }
}

fun String.toDate(format:String): Date {
    return try{
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("Etc/UTC")
        sdf.parse(this)
    }catch (e:Exception){
        Calendar.getInstance().time
    }
}

fun String.toDateTime(): String? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("Etc/UTC")

    val date = try {
        format.parse(this)
    } catch (e: Exception) {
        null
    }
    if (date != null) {
        return when {
            DateUtils.isToday(date.time) -> {
                SimpleDateFormat("HH:mm", Locale.US).format(date)
            }
            else -> {
                SimpleDateFormat("dd/MM, HH:mm", Locale.US).format(date)
            }
        }
    }
    return ""
}