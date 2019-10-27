package com.saiferwp.imgurgallery

import android.app.Application
import com.saiferwp.imgurgallery.di.AppComponent
import com.saiferwp.imgurgallery.di.ContextModule
import com.saiferwp.imgurgallery.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        component = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        lateinit var component: AppComponent private set
    }
}