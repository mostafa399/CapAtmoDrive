package com.mostafahelal.atmodrive2.home.di

import com.mostafahelal.atmodrive2.home.data.data_source.ITripDataSource
import com.mostafahelal.atmodrive2.home.data.data_source.TripApi
import com.mostafahelal.atmodrive2.home.data.data_source.TripDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripDataSourceModule {
    @Provides
    @Singleton
    fun getTripDataSource(tripApiService: TripApi): ITripDataSource
            = TripDataSource(tripApiService)

}