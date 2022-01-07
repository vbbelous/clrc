package com.belous.v.clrc

import android.app.Application
import android.content.Context
import com.belous.v.clrc.di.AppComponent
import com.belous.v.clrc.di.AppModule
import com.belous.v.clrc.di.DaggerAppComponent

class App : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> component
        else -> this.applicationContext.appComponent
    }