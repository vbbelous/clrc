package com.belous.v.clrc.core.data.db

import androidx.room.*
import com.belous.v.clrc.core.domain.Yeelight

@Dao
interface YeelightDao {

    @Query("SELECT * FROM Yeelight")
    suspend fun getAll(): List<Yeelight>

    @Query("SELECT * FROM Yeelight WHERE id = :id")
    suspend fun getById(id: Int): Yeelight

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(Yeelight: Yeelight)

    @Update
    suspend fun update(Yeelight: Yeelight)

    @Update
    suspend fun update(YeelightList: List<Yeelight>)

    @Delete
    suspend fun delete(Yeelight: Yeelight)

    @Query("DELETE FROM Yeelight WHERE id = :id")
    suspend fun deleteById(id: Int)
}