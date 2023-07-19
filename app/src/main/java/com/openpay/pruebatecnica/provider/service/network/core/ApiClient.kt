package com.openpay.pruebatecnica.provider.service.network.core

import com.openpay.pruebatecnica.data.model.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiClient {
    @POST
    suspend fun getData(@Url url:String): Response<ResponseModel>
    @POST
    suspend fun sendData(@Url url: String, @Body data: Any): Response<ResponseModel>
}