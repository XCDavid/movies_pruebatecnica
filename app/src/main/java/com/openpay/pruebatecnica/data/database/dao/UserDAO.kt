package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.Usuario

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: Usuario): Long

    @Update
    suspend fun update(user: Usuario): Int

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT * from user ORDER BY username ASC")
    suspend fun getAll():List<Usuario>

    @Query("SELECT * from user where username = :username")
    suspend fun findByUsername(username: String): Usuario?

}