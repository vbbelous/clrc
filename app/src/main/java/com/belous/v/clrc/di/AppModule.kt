package com.belous.v.clrc.di

import android.content.Context
import androidx.room.Room
import com.belous.v.clrc.model.db.AppDatabase
import com.belous.v.clrc.model.db.ItemDao
import com.belous.v.clrc.presenter.InfoPresenter
import com.belous.v.clrc.presenter.InfoPresenterImpl
import com.belous.v.clrc.presenter.ListPresenter
import com.belous.v.clrc.presenter.ListPresenterImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase): ItemDao {
        return appDatabase.itemDao()
    }

    @Singleton
    @Provides
    fun provideListPresenter(itemDao: ItemDao): ListPresenter {
        return ListPresenterImpl(itemDao)
    }

    @Singleton
    @Provides
    fun provideInfoPresenter(itemDao: ItemDao): InfoPresenter {
        return InfoPresenterImpl(itemDao)
    }
}