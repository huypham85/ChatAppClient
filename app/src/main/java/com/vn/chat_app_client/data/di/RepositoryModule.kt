package com.vn.chat_app_client.data.di

import android.content.SharedPreferences
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.service.AuthService
import com.vn.chat_app_client.data.api.service.RoomService
import com.vn.chat_app_client.data.api.service.UserService
import com.vn.chat_app_client.data.repository.AuthRepositoryImpl
import com.vn.chat_app_client.data.repository.AuthRepositoryMock
import com.vn.chat_app_client.data.repository.RoomRepositoryImpl
import com.vn.chat_app_client.data.repository.UserRepositoryImpl
import com.vn.chat_app_client.domain.repository.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(service: AuthService, prefs: SharedPreferences): AuthRepository =
        AuthRepositoryImpl(
            savedAccountManager = SavedAccountManager(
                prefs
            ), service = service
        )

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()

    @Provides
    @Singleton
    fun provideUserRepository(service :UserService): UserRepository = UserRepositoryImpl(service = service)

    @Provides
    @Singleton
    fun provideRoomRepository(service :RoomService): RoomRepository = RoomRepositoryImpl(service = service)

}

