package com.openpay.pruebatecnica.ui.usecases.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.domian.LoadPopularMovieUseCase
import com.openpay.pruebatecnica.domian.LoadTopRatedMovieUseCase
import com.openpay.pruebatecnica.domian.LoadTrendingMovieUseCase
import com.openpay.pruebatecnica.domian.SaveImagePopularMovieUseCase
import com.openpay.pruebatecnica.domian.SaveImageTopRatedMovieUseCase
import com.openpay.pruebatecnica.domian.SaveImageTrendingMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesFragmentViewModel @Inject constructor(
    private val loadPopularMovieUseCase: LoadPopularMovieUseCase,
    private val saveImagePopularMovieUseCase: SaveImagePopularMovieUseCase,
    private val loadTopRatedMovieUseCase: LoadTopRatedMovieUseCase,
    private val saveImageTopRatedMovieUseCase: SaveImageTopRatedMovieUseCase,
    private val loadTrendingMovieUseCase: LoadTrendingMovieUseCase,
    private val saveImageTrendingMovieUseCase: SaveImageTrendingMovieUseCase
) : ViewModel() {
    val loadTrendingMovieViewModelResponse = MutableLiveData<ResponseRatedMovieModel?>()
    fun loadPopularMovies(): Flow<PagingData<RatedMovieModel>> {
        return Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = { loadPopularMovieUseCase }).flow
    }

    fun saveMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImagePopularMovieUseCase(vo, byteArray)
        }
    }

    fun loadTopRatedMovies(): Flow<PagingData<RatedMovieModel>> {
        return Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = { loadTopRatedMovieUseCase }).flow
    }

    fun saveTopRatedMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImageTopRatedMovieUseCase(vo, byteArray)
        }
    }

    fun loadTrendingMovies() {
       viewModelScope.launch {
           val result = loadTrendingMovieUseCase()
           loadTrendingMovieViewModelResponse.postValue(result)
       }
    }

    fun saveTrendingMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImageTrendingMovieUseCase(vo, byteArray)
        }
    }

}