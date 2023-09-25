package com.mostafahelal.atmodrive2.auth.di

import com.mostafahelal.atmodrive2.auth.data.data_source.remote.IRemoteAuth
import com.mostafahelal.atmodrive2.auth.data.repository.AuthRepository
import com.mostafahelal.atmodrive2.auth.domain.repository.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule  {
    @Provides
    @Singleton
    fun provideRepoModule(iRemoteAuth: IRemoteAuth):IAuthRepository{
        return AuthRepository(iRemoteAuth)
    }
}