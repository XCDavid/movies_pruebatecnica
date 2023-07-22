package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.RatedMovie

@Dao
interface RatedMovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ratedMovie: RatedMovie): Long

    @Update
    suspend fun update(ratedMovie: RatedMovie): Int

    @Query("SELECT * from rated_movie ORDER BY name ASC")
    suspend fun getAll(): List<RatedMovie>

    @Query("DELETE FROM rated_movie")
    suspend fun deleteAll()

    @Query("SELECT * from rated_movie where id = :id")
    suspend fun findById(id: Long): RatedMovie?

}