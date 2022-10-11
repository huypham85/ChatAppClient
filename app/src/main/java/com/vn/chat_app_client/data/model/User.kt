package com.vn.chat_app_client.data.model

import java.io.Serializable


data class User (
    var UserName: String,
    var PhoneNum: String,
    var Password: String
) : Serializable