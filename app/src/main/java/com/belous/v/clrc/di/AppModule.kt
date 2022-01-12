package com.belous.v.clrc.di

import android.app.Application
import androidx.room.Room
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.data.db.AppDatabase
import com.belous.v.clrc.data.db.YeelightDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainStates() = MainStates()

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase): YeelightDao {
        return appDatabase.itemDao()
    }
}