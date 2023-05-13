package com.vn.chat_app_client.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vn.chat_app_client.data.model.Room
import com.vn.chat_app_client.data.model.SampleModel

@Database(entities = [SampleModel::class], version = 1, exportSchema = false)
abstract class LocalMessageData : RoomDatabase() {
    abstract fun getDAO(): LocalMessageDAO
}