package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.PopularMovie

@Dao
interface PopularMovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(popularMovie: PopularMovie): Long

    @Update
    suspend fun update(popularMovie: PopularMovie): Int

    @Query("SELECT * from popular_movie ORDER BY popularity DESC")
    suspend fun getAll(): List<PopularMovie>

    @Query("DELETE FROM popular_movie")
    suspend fun deleteAll()

    @Query("SELECT * from popular_movie where id = :id")
    suspend fun findById(id: Long): PopularMovie?

}