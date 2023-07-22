package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class SaveImageTVUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(ratedTVModel: RatedTVModel, byteArrayIn: ByteArray): Unit {
        val ratedTV = repository.findRatedTVById(ratedTVModel.id)
        if (ratedTV != null) {
            ratedTV.byteArray = byteArrayIn
            repository.updateRatedTV(ratedTV)
        }
    }
}