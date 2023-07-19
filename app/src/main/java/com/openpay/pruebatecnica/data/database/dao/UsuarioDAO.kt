package com.openpay.pruebatecnica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openpay.pruebatecnica.data.database.entity.Usuario

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario): Int

    @Query("DELETE FROM rh_usuario")
    suspend fun deleteAll()

    @Query("SELECT * from rh_usuario ORDER BY username ASC")
    suspend fun getAll():List<Usuario>

    @Query("SELECT * from rh_usuario where username = :username")
    suspend fun findByUsername(username: String): Usuario?

}