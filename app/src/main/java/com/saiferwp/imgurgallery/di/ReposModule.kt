package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.data.GalleryProvider
import com.saiferwp.imgurgallery.api.ApiClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ApiModule::class
    ]
)
open class ReposModule {
    @Provides
    @Singleton
    open fun provideRepos(
        apiClient: ApiClient
    ) = GalleryProvider(apiClient)
}