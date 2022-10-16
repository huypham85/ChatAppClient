package com.vn.chat_app_client.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.vn.chat_app_client.utils.extensions.base64Decoded

class JWTHelper {

    companion object {
        inline fun<reified T> decode(token: String?): T? {
            val jwtComponents = token?.split(".") ?: emptyList()
            if (jwtComponents.size < 2) {
                return null
            }

            return jwtComponents[1].base64Decoded()?.let { dataJSON ->
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                val data = gson.fromJson(dataJSON, T::class.java)
                data
            }
        }
    }

}