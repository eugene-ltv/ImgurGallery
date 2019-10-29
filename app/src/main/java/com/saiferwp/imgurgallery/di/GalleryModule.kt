package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.api.ApiClient
import com.saiferwp.imgurgallery.data.GalleryProvider
import com.saiferwp.imgurgallery.preferences.IPreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ApiModule::class,
        PreferencesModule::class
    ]
)
open class GalleryModule {
    @Provides
    @Singleton
    open fun provideRepos(
        apiClient: ApiClient,
        preferencesManager: IPreferencesManager
    ) = GalleryProvider(apiClient, preferencesManager)
}