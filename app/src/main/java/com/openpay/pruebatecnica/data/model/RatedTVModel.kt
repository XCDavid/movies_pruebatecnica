package com.openpay.pruebatecnica.data.model

import com.google.gson.annotations.SerializedName

data class RatedTVModel (
    @SerializedName("id") val id: Long,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("first_air_date") val firstAirDate: String,
    val byteArray: ByteArray?
)