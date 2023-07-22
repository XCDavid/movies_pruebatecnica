package com.openpay.pruebatecnica.data.model

import com.google.gson.annotations.SerializedName

data class ResponseRatedMovieModel(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val results: List<RatedMovieModel>
)