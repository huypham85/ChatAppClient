package com.vn.chat_app_client.data.di

import android.content.Context
import androidx.room.Room
import com.vn.chat_app_client.data.api.common.Consts.DATABASE_NAME
import com.vn.chat_app_client.data.database.LocalMessageDAO
import com.vn.chat_app_client.data.database.LocalMessageData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun getDatabaseProvide(localMessageData: LocalMessageData):LocalMessageDAO{
        return localMessageData.getDAO()
    }


    @Singleton
    @Provides
    fun provideLocalMessageDatabase(@ApplicationContext context: Context):LocalMessageData{
        return Room.databaseBuilder(context, LocalMessageData::class.java, DATABASE_NAME)
            .build()
    }
}