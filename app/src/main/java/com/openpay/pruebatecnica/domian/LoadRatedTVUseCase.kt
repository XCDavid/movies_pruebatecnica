package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.database.entity.RatedTV
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class LoadRatedTVUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): ResponseRatedTVModel? {
        var response = repository.loadRatedTV()
        if (response != null && response.results.isNullOrEmpty().not()) {
            //save data on data base
            val listRatedTv = response.results
            listRatedTv.forEach {
                repository.saveRatedTV(
                    RatedTV(
                        id = it.id,
                        backdropPath = it.backdropPath,
                        name = it.name,
                        overview = it.overview,
                        posterPath = it.posterPath,
                        rating = it.rating,
                        firstAirDate = it.firstAirDate,
                        byteArray = null
                    )
                )
            }
        } else {
            //get data from data base
            val listRatedTv = repository.getAllRatedsTV()
            if (!listRatedTv.isNullOrEmpty()) {
                val results = ArrayList<RatedTVModel>()
                listRatedTv?.forEach {
                    results.add(
                        RatedTVModel(
                            id = it.id,
                            backdropPath = it.backdropPath,
                            name = it.name,
                            overview = it.overview,
                            posterPath = it.posterPath,
                            rating = it.rating,
                            firstAirDate = it.firstAirDate,
                            byteArray = it.byteArray
                        )
                    )
                }

                response = ResponseRatedTVModel(
                    page = 1, totalPages = 1, totalResults = results.size, results = results
                )
            } else {
                //else return empty result
                response =ResponseRatedTVModel(
                    page = 0, totalPages = 0, totalResults = 0, results = ArrayList<RatedTVModel>()
                )
            }
        }
        return response
    }
}