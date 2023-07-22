package com.openpay.pruebatecnica.ui.usecases.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openpay.pruebatecnica.databinding.FragmentUploadBinding
import com.openpay.pruebatecnica.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : Fragment() {
    companion object {
        val MY_TAG: String = UploadFragment::class.java.simpleName
    }

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private val selectedImageUris = mutableListOf<Uri>()
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)

        storageReference = FirebaseStorage.getInstance().reference
        binding.uploadFragmentUploadButton.setOnClickListener {
            uploadImagesToFirebaseStorage()
        }
        binding.selectImagesButton.setOnClickListener {
            openImagePicker()
        }
        return binding.root
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                if (data.clipData != null) {
                    // Si se seleccionaron varias imágenes
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        selectedImageUris.add(imageUri)
                        var text = binding.uploadImagesList.text.toString()
                        val imageName = Utils.getFileNameFromUri(imageUri, requireContext())
                        val textAdd = "$text\n" + imageName
                        binding.uploadImagesList.text = textAdd
                    }
                } else if (data.data != null) {
                    // Si se seleccionó una sola imagen
                    val imageUri = data.data!!
                    selectedImageUris.add(imageUri)
                    binding.uploadImagesList.text = Utils.getFileNameFromUri(imageUri, requireContext())
                }
            }
        }
    }

    private fun uploadImagesToFirebaseStorage() {
        for (uri in selectedImageUris) {
            val imageName = Utils.getFileNameFromUri(uri, requireContext())
            val imageRef = storageReference.child("images/$imageName.jpg")
            // Subir la imagen a Firebase Storage
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.i(MY_TAG, "Imagen subida exitosamente $imageName")
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e(MY_TAG, exception.message.toString())
                    // Ocurrió un error al subir la imagen
                }
        }
    }
}