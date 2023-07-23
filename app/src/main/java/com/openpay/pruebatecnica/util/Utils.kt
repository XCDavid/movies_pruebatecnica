
package com.openpay.pruebatecnica.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.IOException

class Utils {
    companion object {
        fun getFileNameFromUri(uri: Uri, context: Context): String {
            var displayName = ""
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameColumnIndex != -1) {
                        displayName = it.getString(displayNameColumnIndex)
                        // Aquí tienes el nombre del archivo con su extensión
                        // Puedes procesarlo según tus necesidades
                    } else {
                        // La columna DISPLAY_NAME no está disponible en el cursor
                    }
                }
            }
            return displayName
        }

        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
            val quality: Int = 100
            val stream = ByteArrayOutputStream()
            bitmap.compress(format, quality, stream)
            return stream.toByteArray()
        }

        fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        fun getBitmapFromUri(contentResolver: ContentResolver, imageUri: Uri): Bitmap? {
            return try {
                // Utilizamos el ContentResolver para abrir un flujo de entrada desde la Uri
                val inputStream = contentResolver.openInputStream(imageUri)
                // Decodificamos el flujo de entrada en un Bitmap
                BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

    }
}