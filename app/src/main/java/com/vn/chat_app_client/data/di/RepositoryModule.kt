package com.vn.chat_app_client.data.di

import android.content.SharedPreferences
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.data.api.service.AuthService
import com.vn.chat_app_client.data.repository.AuthRepositoryImpl
import com.vn.chat_app_client.data.repository.AuthRepositoryMock
import com.vn.chat_app_client.domain.repository.repository.AuthRepository
import com.vn.chat_app_client.domain.repository.repository.MessageRepository
import com.vn.chat_app_client.domain.repository.repository.MessageRepositoryImpl
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
    fun provideUserRepository(service: AuthService, prefs: SharedPreferences): AuthRepository =
        AuthRepositoryImpl(
            savedAccountManager = SavedAccountManager(
                prefs
            ), service = service
        )

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()

}

