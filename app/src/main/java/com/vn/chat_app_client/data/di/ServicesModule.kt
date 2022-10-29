package com.vn.chat_app_client.data.di

import com.vn.chat_app_client.data.api.service.AuthService
import com.vn.chat_app_client.data.api.service.ProfileService
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
}