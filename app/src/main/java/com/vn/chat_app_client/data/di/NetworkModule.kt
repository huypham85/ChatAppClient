package com.vn.chat_app_client.data.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vn.chat_app_client.R
import com.vn.chat_app_client.data.api.common.Consts
import com.vn.chat_app_client.data.api.common.SavedAccountManager
import com.vn.chat_app_client.utils.AppEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideAccountData(
        prefs: SharedPreferences,
    ): SavedAccountManager {
        return SavedAccountManager(prefs)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(
        savedAccountManager: SavedAccountManager,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val authorization =
                    savedAccountManager.fetchAuthToken()?.let { "${Consts.JWT_PREFIX}$it" }
                val request = chain.request().let {
                    if (authorization == null) it
                    else {
                        it.newBuilder().addHeader(Consts.AUTHORIZATION_KEY, authorization).build()
                    }
                }
                Log.d("ACCESS_TOKEN", savedAccountManager.fetchAuthToken().toString())
                Log.d("HEADER", authorization.toString())
                chain.proceed(request).apply {
                    if (code == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        EventBus.getDefault().post(AppEvent.LogOut)
                    }
                }
            }
            .dispatcher(Dispatcher().apply { maxRequests = 1 })
            .apply {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(interceptor)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
    }
}