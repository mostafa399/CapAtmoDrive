package com.mostafahelal.atmodrive2.auth.di

import com.mostafahelal.atmodrive2.auth.domain.repository.IAuthRepository
import com.mostafahelal.atmodrive2.auth.domain.use_case.AuthUseCase
import com.mostafahelal.atmodrive2.auth.domain.use_case.IAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideUseCase(iAuthRepository: IAuthRepository):IAuthUseCase{
        return AuthUseCase(iAuthRepository)
    }
}