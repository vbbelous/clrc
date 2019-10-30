package com.belous.v.clrc.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.belous.v.clrc.model.Item;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    Observable<List<Item>> getAll();

    @Query("SELECT * FROM item WHERE id = :id")
    Observable<Item> get(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);

    @Update
    void update(Item item);

    @Update
    void update(List<Item> items);

    @Delete
    void delete(Item item);
}
