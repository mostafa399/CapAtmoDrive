package com.mostafahelal.atmodrive2.auth.di

import com.mostafahelal.atmodrive2.auth.data.data_source.ApiService
import com.mostafahelal.atmodrive2.auth.data.data_source.GeneralService
import com.mostafahelal.atmodrive2.auth.data.data_source.remote.IRemoteAuth
import com.mostafahelal.atmodrive2.auth.data.data_source.remote.RemoteAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule  {
    @Provides
    @Singleton
    fun provideRemoteData(apiService: ApiService,
                          generalService: GeneralService
    ):IRemoteAuth{
        return RemoteAuth(apiService,generalService)
    }
}