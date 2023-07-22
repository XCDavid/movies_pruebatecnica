package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class SaveImageMovieUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(ratedMovieModel: RatedMovieModel, byteArrayIn: ByteArray): Unit {
        val ratedMovie = repository.findRatedMovieById(ratedMovieModel.id)
        if (ratedMovie != null) {
            ratedMovie.byteArray = byteArrayIn
            repository.updateRatedMovie(ratedMovie)
        }
    }
}