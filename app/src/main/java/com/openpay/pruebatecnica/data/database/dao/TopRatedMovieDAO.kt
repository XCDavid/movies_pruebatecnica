package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.TopRatedMovie

@Dao
interface TopRatedMovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topRatedMovie: TopRatedMovie): Long

    @Update
    suspend fun update(topRatedMovie: TopRatedMovie): Int

    @Query("SELECT * from top_rated_movie ORDER BY voteAverage DESC, id ASC")
    suspend fun getAll(): List<TopRatedMovie>

    @Query("DELETE FROM top_rated_movie")
    suspend fun deleteAll()

    @Query("SELECT * from top_rated_movie where id = :id")
    suspend fun findById(id: Long): TopRatedMovie?

}