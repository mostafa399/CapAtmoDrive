package com.mostafahelal.atmodrive2.auth.di

import android.content.SharedPreferences
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.presentation.view_model.AuthViewModel
import com.mostafahelal.atmodrive2.auth.presentation.view_model.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule  {
    @Provides
    fun provideSplashViewModel(iSharedPreferencesManager: ISharedPreferencesManager):SplashViewModel{
        return SplashViewModel(iSharedPreferencesManager)
    }
}