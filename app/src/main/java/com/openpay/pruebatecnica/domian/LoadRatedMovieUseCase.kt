package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.database.entity.RatedMovie
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class LoadRatedMovieUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): ResponseRatedMovieModel? {
        var response = repository.loadRatedMovie()

        if (response != null && response.results.isNullOrEmpty().not()) {
            //save data on data base
            val listRatedMovie = response.results
            listRatedMovie.forEach {
                repository.saveRatedMovie(
                    RatedMovie(
                        id = it.id,
                        backdropPath = it.backdropPath,
                        name = it.name,
                        overview = it.overview,
                        posterPath = it.posterPath,
                        rating = it.rating,
                        byteArray = it.byteArray
                    )
                )
            }
        } else {
            //get data from data base
            val listRatedMovie = repository.getAllRatedsMovie()

            if (listRatedMovie.isNullOrEmpty().not()) {
                //if has rated movie data build ResponseRatedMovieModel
                var results = ArrayList<RatedMovieModel>()
                listRatedMovie?.forEach {
                    results.add(
                        RatedMovieModel(
                            id = it.id,
                            backdropPath = it.backdropPath,
                            name = it.name,
                            overview = it.overview,
                            posterPath = it.posterPath,
                            rating = it.rating,
                            popularity = null,
                            voteAverage = null,
                            byteArray = it.byteArray
                        )
                    )
                }

                response = ResponseRatedMovieModel(
                    page = 1,
                    totalPages = 1,
                    totalResults = results.size,
                    results = results
                )
            } else {
                //else return empty result
                response =  ResponseRatedMovieModel(
                    page = 0,
                    totalPages = 0,
                    totalResults = 0,
                    results = ArrayList<RatedMovieModel>()
                )
            }
        }

        return response
    }
}