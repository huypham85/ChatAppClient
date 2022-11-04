package com.vn.chat_app_client.data.api.common

import android.content.SharedPreferences
import com.vn.chat_app_client.utils.JWTHelper
import javax.inject.Inject

class SavedAccountManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    val data: AccountData?
        get() = JWTHelper.decode(fetchAuthToken())

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}

//data class AccountData(
//    val username: String,
//    val displayName: String,
//)
