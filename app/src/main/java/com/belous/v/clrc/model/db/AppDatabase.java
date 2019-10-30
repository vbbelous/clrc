package com.belous.v.clrc.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.belous.v.clrc.model.Item;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "database";

    public abstract ItemDao itemDao();
}
