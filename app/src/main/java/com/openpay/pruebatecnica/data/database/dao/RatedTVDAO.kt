package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.RatedTV

@Dao
interface RatedTVDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ratedTv: RatedTV): Long

    @Update
    suspend fun update(ratedTv: RatedTV): Int

    @Query("SELECT * from rated_tv ORDER BY name ASC")
    suspend fun getAll(): List<RatedTV>

    @Query("DELETE FROM rated_tv")
    suspend fun deleteAll()

    @Query("SELECT * from rated_tv where id = :id")
    suspend fun findById(id: Long): RatedTV?

}