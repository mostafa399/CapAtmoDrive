package com.mostafahelal.atmodrive2.home.di

import com.mostafahelal.atmodrive2.home.domain.repository.ITripRepository
import com.mostafahelal.atmodrive2.home.domain.use_case.ITripUseCase
import com.mostafahelal.atmodrive2.home.domain.use_case.TripUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripUseCaseModule {
    @Provides
    @Singleton
    fun MakeTripUseCase(iTripRepo: ITripRepository): ITripUseCase
            = TripUseCase(iTripRepo)

}