package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.TrendingMovie

@Dao
interface TrendingMovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trendingMovie: TrendingMovie): Long

    @Update
    suspend fun update(trendingMovie: TrendingMovie): Int

    @Query("SELECT * from trending_movie ORDER BY auto_id ASC")
    suspend fun getAll(): List<TrendingMovie>

    @Query("DELETE FROM trending_movie")
    suspend fun deleteAll()

    @Query("SELECT * from trending_movie where id = :id")
    suspend fun findById(id: Long): TrendingMovie?

}