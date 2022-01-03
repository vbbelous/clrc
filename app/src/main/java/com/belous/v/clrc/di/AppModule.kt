package com.belous.v.clrc.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.belous.v.clrc.core.data.db.AppDatabase
import com.belous.v.clrc.core.data.db.YeelightDao
import com.belous.v.clrc.MainStates
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideMainStates() = MainStates()

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase): YeelightDao {
        return appDatabase.itemDao()
    }
}