package com.belous.v.clrc.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.belous.v.clrc.core.data.db.entity.YeelightEntity
import com.belous.v.clrc.core.data.db.entity.ParamsConverter

@Database(entities = [YeelightEntity::class], version = 1)
@TypeConverters(ParamsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "database"
    }

    abstract fun itemDao(): YeelightDao
}