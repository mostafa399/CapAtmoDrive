package com.mostafahelal.atmodrive2.home.di

import com.mostafahelal.atmodrive2.home.data.data_source.ITripDataSource
import com.mostafahelal.atmodrive2.home.data.repository.TripRepository
import com.mostafahelal.atmodrive2.home.domain.repository.ITripRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripRepositoryModule {


    @Provides
    @Singleton
    fun provideTripRepo(iTripDataSource: ITripDataSource): ITripRepository
            = TripRepository(iTripDataSource)


}