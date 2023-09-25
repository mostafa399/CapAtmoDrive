package com.mostafahelal.atmodrive2.atmo_di

import com.mostafahelal.atmodrive2.auth.data.data_source.ApiService
import com.mostafahelal.atmodrive2.auth.data.data_source.GeneralService
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("apiCaptain")
    fun provideOkhttp(iSharedPreferencesManager:ISharedPreferencesManager): Retrofit {
        val client= OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder().build()
                    val requestBuilder = originalRequest.newBuilder().url(url)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer ${iSharedPreferencesManager.getString(Constants.REMEMBER_TOKEN_PREFS)}")
                        .addHeader("Accept-Language","en")
                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code
                    return response
                } }).build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_CAPTAIN_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideApiService(@Named("apiCaptain") retrofit: Retrofit): ApiService =retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    @Named("apiUploadImage")
    fun provideOkHttpClient(): Retrofit {
        val client= OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_Upload_image_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeneralService(@Named("apiUploadImage")retrofit: Retrofit): GeneralService = retrofit.create(GeneralService::class.java)


}