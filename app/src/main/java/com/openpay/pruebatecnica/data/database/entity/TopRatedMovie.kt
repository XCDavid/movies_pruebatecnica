package com.openpay.pruebatecnica.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_rated_movie")
data class TopRatedMovie(
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
    @ColumnInfo(name = "popularity")
    val popularity: Float?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Float?,
    @ColumnInfo(name = "image")
    var byteArray: ByteArray?
)