package com.saiferwp.imgurgallery.di

import android.content.Context
import android.content.SharedPreferences
import com.saiferwp.imgurgallery.BuildConfig
import com.saiferwp.imgurgallery.preferences.IPreferencesManager
import com.saiferwp.imgurgallery.preferences.PreferencesManager
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        ContextModule::class
    ]
)
class PreferencesModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    @Provides
    fun providePreferences(sharedPreferences: SharedPreferences): IPreferencesManager =
        PreferencesManager(sharedPreferences)
}
