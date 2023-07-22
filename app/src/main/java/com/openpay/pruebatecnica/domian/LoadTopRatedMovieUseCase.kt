package com.openpay.pruebatecnica.domian

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.openpay.pruebatecnica.data.database.entity.PopularMovie
import com.openpay.pruebatecnica.data.database.entity.TopRatedMovie
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class LoadTopRatedMovieUseCase @Inject constructor(private val repository: Repository) : PagingSource<Int, RatedMovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, RatedMovieModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RatedMovieModel> {
        try {
            // Obtener el número de página a cargar
            val pageNumber = params.key ?: 1
            // Llamar a la función que carga las películas desde el repositorio
            val response = repository.loadTopRatedrMovies(pageNumber)
            var nextPage: Int? = null
            // Obtener la lista de películas desde la respuesta
            var movies: List<RatedMovieModel>? = null
            if (response != null && response.results.isNullOrEmpty().not()) {
                movies = response.results
                saveInDB(movies)
                // Calcular la siguiente página a cargar (si la hay)
                nextPage = if (response.page < response.totalPages) response.page + 1 else null
            } else {
                movies = loadfromDB()
            }
            // Devolver el resultado con la lista de películas y la siguiente página
            return LoadResult.Page(data = movies, prevKey = null, nextKey = nextPage)
        } catch (e: Exception) {
            // Manejar errores aquí
            return LoadResult.Error(e)
        }
    }

    private suspend fun saveInDB(listRatedMovie: List<RatedMovieModel>) {
        listRatedMovie.forEach {
            repository.saveTopRatedMovie(
                TopRatedMovie(
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
    }

    private suspend fun loadfromDB(): ArrayList<RatedMovieModel> {
        //get data from data base
        val listRatedMovie = repository.getAllTopRatedMovie()
        return if (listRatedMovie.isNullOrEmpty().not()) {
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
                        popularity = it.popularity,
                        voteAverage = it.voteAverage,
                        byteArray = it.byteArray
                    )
                )
            }
            results
        } else {
            //else return empty result
            ArrayList<RatedMovieModel>()

        }
    }

}