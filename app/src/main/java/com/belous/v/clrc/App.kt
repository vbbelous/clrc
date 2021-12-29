package com.belous.v.clrc

import android.app.Application
import com.belous.v.clrc.di.AppComponent
import com.belous.v.clrc.di.AppModule
import com.belous.v.clrc.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}