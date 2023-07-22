package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import javax.inject.Inject

class SaveImageProfileUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(responseProfileModel: ResponseProfileModel, byteArray: ByteArray) {
        val profile = repository.findProfileById(responseProfileModel.id)
        if (profile != null) {
            profile.avatar = byteArray
            repository.updateProfile(profile)
        }
    }
}