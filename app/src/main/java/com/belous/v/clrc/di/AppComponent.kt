package com.belous.v.clrc.di

import com.belous.v.clrc.view.dialog.BaseDialog
import com.belous.v.clrc.view.dialog.FoundDialog
import com.belous.v.clrc.view.fragment.InfoFragment
import com.belous.v.clrc.view.fragment.ListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(foundDialog: FoundDialog)

    fun inject(baseDialog: BaseDialog)

    fun inject(listFragment: ListFragment)

    fun inject(infoFragment: InfoFragment)
}