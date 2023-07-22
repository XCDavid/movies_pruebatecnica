package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class SaveImageTrendingMovieUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(ratedMovieModel: RatedMovieModel, byteArrayIn: ByteArray): Unit {
        val trendingMovie = repository.findTrendingMovieById(ratedMovieModel.id)
        if (trendingMovie != null) {
            trendingMovie.byteArray = byteArrayIn
            repository.updateTrendingMovie(trendingMovie)
        }
    }
}