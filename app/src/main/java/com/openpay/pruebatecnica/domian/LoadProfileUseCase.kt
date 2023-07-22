package com.openpay.pruebatecnica.domian

import com.openpay.pruebatecnica.data.database.entity.Profile
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.provider.respository.Repository
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import com.squareup.picasso.Picasso
import java.lang.IllegalStateException
import javax.inject.Inject

class LoadProfileUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): ResponseProfileModel? {
        var response = repository.loadProfile()
        if (response != null) {
            //Guardar actualizar datos de perfil en la base de datos
            val profileToDatabase = response.toProfile()
            val profileConsult = repository.findProfileById(response.id)
            if (profileConsult != null) repository.updateProfile(profileToDatabase)
            else repository.saveProfile(profileToDatabase)
        } else {
            //Obtener datos del perfil desde base de datos y mapearlo a ResponseProfileModel
            val profiles = repository.getProfiles()
            if (!profiles.isNullOrEmpty()) {
                response = profiles?.first()?.toResponseProfileModel() //?: throw IllegalStateException("No Profiles on data base")
            }
        }
        return response
    }

    private fun ResponseProfileModel.toProfile(): Profile {
        return Profile(
            id = this.id,
            avatar = null,
            name = this.name,
            username = this.username,
            iso6391 = this.iso6391,
            iso31661 = this.iso31661
        )
    }

    private fun Profile.toResponseProfileModel(): ResponseProfileModel {
        return ResponseProfileModel(
            id = this.id,
            name = this.name,
            avatar = null,
            username = this.username,
            iso6391 = this.iso6391,
            iso31661 = this.iso31661,
            byteArray = this.avatar
        )
    }
}