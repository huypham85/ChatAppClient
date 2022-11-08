package com.vn.chat_app_client.data.di

import com.vn.chat_app_client.data.api.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Singleton
    @Provides
    fun provideAuthServices(
        retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideProfileServices(
        retrofit: Retrofit
    ): ProfileService = retrofit.create(ProfileService::class.java)

    @Singleton
    @Provides
    fun provideUserServices(
        retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideRoomServices(
        retrofit: Retrofit
    ): RoomService = retrofit.create(RoomService::class.java)

    @Singleton
    @Provides
    fun provideAttachmentServices(
        retrofit: Retrofit
    ): AttachmentService = retrofit.create(AttachmentService::class.java)
}