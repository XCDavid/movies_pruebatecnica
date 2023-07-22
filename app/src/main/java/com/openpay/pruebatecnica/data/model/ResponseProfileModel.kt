package com.openpay.pruebatecnica.data.model

import com.google.gson.annotations.SerializedName

data class ResponseProfileModel(
    @SerializedName("id") val id: Long,
    @SerializedName("avatar") val avatar: AvatarModel?,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("iso_639_1") val iso6391 : String,
    @SerializedName("iso_3166_1") val iso31661 : String,
    val byteArray: ByteArray?
)
