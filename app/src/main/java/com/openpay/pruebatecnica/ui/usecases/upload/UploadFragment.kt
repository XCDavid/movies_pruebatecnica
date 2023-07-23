package com.openpay.pruebatecnica.ui.usecases.upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openpay.pruebatecnica.data.model.ImageUploadModel
import com.openpay.pruebatecnica.databinding.FragmentUploadBinding
import com.openpay.pruebatecnica.ui.common.MyDialogFragment
import com.openpay.pruebatecnica.ui.usecases.profile.ProfileFragment
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : Fragment() {
    companion object {
        val MY_TAG: String = UploadFragment::class.java.simpleName
    }

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
//    private val selectedImageUris = mutableListOf<Uri>()
    private var imageUploadModelList: MutableList<ImageUploadModel> = ArrayList<ImageUploadModel>()
    private lateinit var storageReference: StorageReference
    private var listAdapter: ImageUploadAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val emptyList: List<ImageUploadModel> = emptyList()
        listAdapter = ImageUploadAdapter(emptyList){vo -> onItemSelected(vo)}
        binding.uploadImagesList.layoutManager = LinearLayoutManager(context)
        binding.uploadImagesList.adapter = listAdapter

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
                        val imageName = Utils.getFileNameFromUri(imageUri, requireContext())
//                        selectedImageUris.add(imageUri)
//                        var text = binding.uploadImagesList.text.toString()
//                        val imageName = Utils.getFileNameFromUri(imageUri, requireContext())
//                        val textAdd = "$text\n" + imageName
//                        binding.uploadImagesList.text = textAdd
                        imageUploadModelList.add(ImageUploadModel(i, imageName, imageUri, 0))
                    }
                } else if (data.data != null) {
                    // Si se seleccionó una sola imagen
                    val imageUri = data.data!!
//                    selectedImageUris.add(imageUri)
                    val imageName = Utils.getFileNameFromUri(imageUri, requireContext())
//                    binding.uploadImagesList.text = Utils.getFileNameFromUri(imageUri, requireContext())
                    imageUploadModelList.add(ImageUploadModel(0, imageName, imageUri, 0))
                }
                listAdapter?.updateList(imageUploadModelList.toList())
            }
        }
    }

    private fun onItemSelected(vo: ImageUploadModel) {
        Log.i(ProfileFragment.MY_TAG, "onItemSelected: ${vo.name}}")
    }

    private fun uploadImagesToFirebaseStorage() {
        for (imageUploadModel in imageUploadModelList) {
            val imageName = Utils.getFileNameFromUri(imageUploadModel.uri, requireContext())
            val imageRef = storageReference.child("images/$imageName.jpg")
            // Subir la imagen a Firebase Storage
            val uploadTask = imageRef.putFile(imageUploadModel.uri)

            // Agregar listener para obtener el progreso de la subida
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                Log.i(MY_TAG, "Progreso de la subida de $imageName: $progress%")
                listAdapter?.updateProgress(imageUploadModel.id, progress)
            }

            // Agregar listener para la finalización exitosa de la subida
            uploadTask.addOnSuccessListener {
                Log.i(MY_TAG, "Imagen subida exitosamente $imageName")
            }

            // Agregar listener para el error de la subida
            uploadTask.addOnFailureListener { exception: Exception ->
                Log.e(MY_TAG, exception.message.toString())
                // Ocurrió un error al subir la imagen
                val dialogFragment = MyDialogFragment()
                dialogFragment.arguments = Bundle().apply {
                    putString(Constants.INTENT_FRAGMENT_MESSAGE, "Error uploading the image.")
                }
                dialogFragment.show(requireActivity().supportFragmentManager, "myDialog")

            }
        }
    }

}