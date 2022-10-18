package com.vn.chat_app_client.data.api.common

import com.vn.chat_app_client.utils.JWTHelper

data class SavedAccount(var accessToken: String? = null) {
    val data: AccountData?
        get() = JWTHelper.decode(accessToken)
}

data class AccountData(
    val username: String,
    val displayName: String,
)
