package com.openpay.pruebatecnica.provider.service.network.core

import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiClient {
    @GET
    suspend fun getDataRatedTV(@Url url: String): Response<ResponseRatedTVModel>

    @GET
    suspend fun getDataProfile(@Url url: String): Response<ResponseProfileModel>

    @GET
    suspend fun getDataMovies(@Url url: String): Response<ResponseRatedMovieModel>

}