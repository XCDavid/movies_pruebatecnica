package com.openpay.pruebatecnica.provider.service.network

import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import com.openpay.pruebatecnica.provider.service.network.core.ApiClient
import com.openpay.pruebatecnica.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkService @Inject constructor(private val apiClient: ApiClient) {

    suspend fun loadProfile(): ResponseProfileModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseProfileModel? = null
            try {
                val responseApi = apiClient.getDataProfile(Constants.ACCOUNT_DETAILS)
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun loadRatedTV(): ResponseRatedTVModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseRatedTVModel? = null
            try {
                val responseApi = apiClient.getDataRatedTV(Constants.RATED_TV)
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun loadRatedMovie(): ResponseRatedMovieModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseRatedMovieModel? = null
            try {
                val responseApi = apiClient.getDataMovies(Constants.RATED_MOVIES)
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun loadPopularMovies(pageNumber: Int): ResponseRatedMovieModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseRatedMovieModel? = null
            try {
                val responseApi = apiClient.getDataMovies(Constants.POPULAR_MOVIES.format(pageNumber))
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }
    suspend fun loadTopRatedMovies(pageNumber: Int): ResponseRatedMovieModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseRatedMovieModel? = null
            try {
                val responseApi = apiClient.getDataMovies(Constants.TOP_RATED_MOVIES.format(pageNumber))
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun loadTrendingMovie(): ResponseRatedMovieModel? {
        return withContext(Dispatchers.IO) {
            var response: ResponseRatedMovieModel? = null
            try {
                val responseApi = apiClient.getDataMovies(Constants.TRENDING_MOVIES)
                response = responseApi.body()
            } catch (e: Exception) {//Unknown host exception
                e.printStackTrace()
            }
            response
        }
    }

}