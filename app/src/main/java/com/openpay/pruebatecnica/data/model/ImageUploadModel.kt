package com.openpay.pruebatecnica.data.model

import android.net.Uri

data class ImageUploadModel(
    val id: Int,
    val name: String,
    val uri: Uri,
    var progress : Int
)
