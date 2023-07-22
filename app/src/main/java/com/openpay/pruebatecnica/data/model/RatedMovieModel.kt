package com.openpay.pruebatecnica.data.model

import com.google.gson.annotations.SerializedName

data class RatedMovieModel(
    @SerializedName("id") val id: Long,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("original_title") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("popularity") val popularity: Float?,
    @SerializedName("vote_average") val voteAverage: Float?,
    val byteArray: ByteArray?
)