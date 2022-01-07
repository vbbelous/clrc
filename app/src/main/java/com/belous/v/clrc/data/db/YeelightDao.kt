package com.belous.v.clrc.data.db

import androidx.room.*
import com.belous.v.clrc.data.db.entity.YeelightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YeelightDao {

    @Query("SELECT * FROM Yeelight")
    fun getAll(): Flow<List<YeelightEntity>>

    @Query("SELECT * FROM Yeelight WHERE id = :id")
    suspend fun getById(id: Int): YeelightEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(YeelightEntity: YeelightEntity)

    @Update
    suspend fun update(YeelightEntity: YeelightEntity)

    @Update
    suspend fun update(yeelightEntityList: List<YeelightEntity>)

    @Delete
    suspend fun delete(YeelightEntity: YeelightEntity)

    @Query("DELETE FROM Yeelight WHERE id = :id")
    suspend fun deleteById(id: Int)
}