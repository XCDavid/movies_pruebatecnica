package com.openpay.pruebatecnica.data.database

import androidx.room.Database
import com.openpay.pruebatecnica.data.database.dao.UsuarioDAO
import com.openpay.pruebatecnica.data.database.entity.Usuario

@Database(entities = [Usuario::class], version = 3, exportSchema = false)
abstract class RoomDatabase : androidx.room.RoomDatabase() {
    abstract fun getUsuarioDAO(): UsuarioDAO
}