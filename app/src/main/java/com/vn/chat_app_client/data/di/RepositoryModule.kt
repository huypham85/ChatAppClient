package com.vn.chat_app_client.data.di

import com.vn.chat_app_client.data.api.AuthService
import com.vn.chat_app_client.data.repository.AuthRepository
import com.vn.chat_app_client.data.repository.AuthRepositoryImpl
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
    fun provideUserRepository(service: AuthService): AuthRepository = AuthRepositoryImpl(service)
}