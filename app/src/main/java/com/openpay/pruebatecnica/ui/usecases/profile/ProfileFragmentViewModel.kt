package com.openpay.pruebatecnica.ui.usecases.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import com.openpay.pruebatecnica.domian.LoadProfileUseCase
import com.openpay.pruebatecnica.domian.LoadRatedMovieUseCase
import com.openpay.pruebatecnica.domian.LoadRatedTVUseCase
import com.openpay.pruebatecnica.domian.SaveImageMovieUseCase
import com.openpay.pruebatecnica.domian.SaveImageProfileUseCase
import com.openpay.pruebatecnica.domian.SaveImageTVUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val loadProfileUseCase: LoadProfileUseCase,
    private val loadRatedTVUseCase: LoadRatedTVUseCase,
    private val loadRatedMovieUseCase: LoadRatedMovieUseCase,
    private val saveImageProfileUseCase: SaveImageProfileUseCase,
    private val saveImageTVUseCase: SaveImageTVUseCase,
    private val saveImageMovieUseCase: SaveImageMovieUseCase
) : ViewModel() {
    val lodaProfileViewModelResponse = MutableLiveData<ResponseProfileModel?>()
    val lodaRatedTVViewModelResponse = MutableLiveData<ResponseRatedTVModel?>()
    val loadRatedMovieViewModelResponse = MutableLiveData<ResponseRatedMovieModel?>()

    fun loadProfile() {
        viewModelScope.launch {
            val result = loadProfileUseCase()
            lodaProfileViewModelResponse.postValue(result)
        }
    }

    fun loadRatedTV() {
        viewModelScope.launch {
            val result = loadRatedTVUseCase()
            lodaRatedTVViewModelResponse.postValue(result)
        }
    }

    fun loadRatedMovie() {
        viewModelScope.launch {
            val result = loadRatedMovieUseCase()
            loadRatedMovieViewModelResponse.postValue(result)
        }
    }

    fun saveImageProfile(responseModel: ResponseProfileModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImageProfileUseCase(responseModel, byteArray)
        }
    }

    fun saveImageTV(vo: RatedTVModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImageTVUseCase(vo, byteArray)
        }
    }

    fun saveMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        viewModelScope.launch {
            saveImageMovieUseCase(vo, byteArray)
        }
    }

}