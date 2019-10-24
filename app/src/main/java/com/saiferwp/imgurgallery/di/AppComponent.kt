package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.data.GalleryProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        ReposModule::class
    ]
)
interface AppComponent {
    fun getReposProvider(): GalleryProvider
}
