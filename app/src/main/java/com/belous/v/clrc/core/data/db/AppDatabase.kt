package com.belous.v.clrc.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.belous.v.clrc.core.domain.Yeelight
import com.belous.v.clrc.core.data.db.entity.YeelightConverter

@Database(entities = [Yeelight::class], version = 1)
@TypeConverters(YeelightConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "database"
    }

    abstract fun itemDao(): YeelightDao
}