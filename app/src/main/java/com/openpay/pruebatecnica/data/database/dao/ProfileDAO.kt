package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.Profile

@Dao
interface ProfileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile): Long

    @Update
    suspend fun update(profile: Profile): Int

    @Query("DELETE FROM profile")
    suspend fun deleteAll()

    @Query("SELECT * from profile ORDER BY id ASC")
    suspend fun getAll(): List<Profile>

    @Query("SELECT * from profile where id = :id")
    suspend fun findById(id: Long): Profile?

}