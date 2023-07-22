package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.database.entity.RatedMovie
import com.openpay.pruebatecnica.data.database.entity.TrendingMovie
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class LoadTrendingMovieUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): ResponseRatedMovieModel? {
        var response = repository.loadTrendingMovie()

        if (response != null && response.results.isNullOrEmpty().not()) {
            //save data on data base
            val listRatedMovie = response.results
            listRatedMovie.forEach {
                repository.saveTrendingMovie(
                    TrendingMovie(
                        id = it.id,
                        backdropPath = it.backdropPath,
                        name = it.name,
                        overview = it.overview,
                        posterPath = it.posterPath,
                        rating = it.rating,
                        popularity = it.popularity,
                        voteAverage = it.voteAverage,
                        byteArray = it.byteArray
                    )
                )
            }
        } else {
            //get data from data base
            val listTrendingMovie = repository.getAllTrendingMovies()

            if (listTrendingMovie.isNullOrEmpty().not()) {
                //if has rated movie data build ResponseRatedMovieModel
                var results = ArrayList<RatedMovieModel>()
                listTrendingMovie?.forEach {
                    results.add(
                        RatedMovieModel(
                            id = it.id,
                            backdropPath = it.backdropPath,
                            name = it.name,
                            overview = it.overview,
                            posterPath = it.posterPath,
                            rating = it.rating,
                            popularity = it.popularity,
                            voteAverage = it.voteAverage,
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