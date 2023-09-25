package com.mostafahelal.atmodrive2.atmo_di

import android.content.Context
import android.content.SharedPreferences
import com.mostafahelal.atmodrive2.auth.data.data_source.local.ISharedPreferencesManager
import com.mostafahelal.atmodrive2.auth.data.data_source.local.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPreferencesManager.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }
    @Singleton
    @Provides
    fun providesSharedPreferencesManager(sharedPreferences: SharedPreferences): ISharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences)
    }
}