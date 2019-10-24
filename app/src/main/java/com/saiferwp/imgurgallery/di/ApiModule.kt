package com.saiferwp.imgurgallery.di

import com.saiferwp.imgurgallery.data.api.ApiClient
import com.saiferwp.imgurgallery.data.api.ApiFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        val factory = ApiFactory()
        val api = factory.create()
        return ApiClient(api)
    }
}
