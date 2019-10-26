package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.data.GalleryProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        GalleryModule::class
    ]
)
interface AppComponent {
    fun getGalleryProvider(): GalleryProvider
}
