package com.openpay.pruebatecnica.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rated_tv")
data class RatedTV(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "backdropPath")
    val backdropPath: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "posterPath")
    val posterPath: String,
    @ColumnInfo(name = "rating")
    val rating: Int,
    @ColumnInfo(name = "firstAirDate")
    val firstAirDate: String,
    @ColumnInfo(name = "image")
    var byteArray: ByteArray?
)