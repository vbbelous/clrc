package com.belous.v.clrc.di

import com.belous.v.clrc.ui.feature_main.MainFragment
import com.belous.v.clrc.ui.feature_yeelight.YeelightFragment
import com.belous.v.clrc.MainActivity
import com.belous.v.clrc.ui.dialog.FoundDialog
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(foundDialog: FoundDialog)

    fun inject(mainFragment: MainFragment)

    fun inject(yeelightFragment: YeelightFragment)
}