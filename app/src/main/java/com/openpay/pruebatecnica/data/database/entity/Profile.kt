package com.openpay.pruebatecnica.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "avatar")
    var avatar: ByteArray?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "iso6391")
    val iso6391 : String,
    @ColumnInfo(name = "iso31661")
    val iso31661 : String
)
