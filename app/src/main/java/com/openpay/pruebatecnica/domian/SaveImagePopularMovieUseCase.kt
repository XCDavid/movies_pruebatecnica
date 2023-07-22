package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class SaveImagePopularMovieUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(ratedMovieModel: RatedMovieModel, byteArrayIn: ByteArray): Unit {
        val popularMovie = repository.findPopularMovieById(ratedMovieModel.id)
        if (popularMovie != null) {
            popularMovie.byteArray = byteArrayIn
            repository.updatePopularMovie(popularMovie)
        }
    }
}