package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.data.GalleryProvider
import com.saiferwp.imgurgallery.preferences.IPreferencesManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        ApiModule::class,
        GalleryModule::class,
        PreferencesModule::class
    ]
)
interface AppComponent {
    fun getGalleryProvider(): GalleryProvider
    fun getPreferencesManager() : IPreferencesManager
}
