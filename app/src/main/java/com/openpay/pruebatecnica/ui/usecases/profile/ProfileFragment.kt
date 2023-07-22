package com.openpay.pruebatecnica.ui.usecases.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import com.openpay.pruebatecnica.databinding.FragmentProfileBinding
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        val MY_TAG: String = ProfileFragment::class.java.simpleName
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.reciclerViewTV.layoutManager = LinearLayoutManager(context)
        binding.reciclerViewMovie.layoutManager = LinearLayoutManager(context)

        profileFragmentViewModel.lodaProfileViewModelResponse.observe(viewLifecycleOwner) {
            validateResponseLoadProfile(it)
        }
        profileFragmentViewModel.lodaRatedTVViewModelResponse.observe(viewLifecycleOwner) {
            validateResponseLoadRatedTV(it)
        }
        profileFragmentViewModel.loadRatedMovieViewModelResponse.observe(viewLifecycleOwner) {
            validateResponseLoadRatedMovie(it)
        }
        profileFragmentViewModel.loadProfile()
        profileFragmentViewModel.loadRatedTV()
        profileFragmentViewModel.loadRatedMovie()
        return binding.root
    }

    private fun validateResponseLoadRatedTV(responseModel: ResponseRatedTVModel?) {
        responseModel?.let { responseModel ->
            val ratedTVList = responseModel.results
            ratedTVList?.let { dataLst ->
                if (dataLst.isNotEmpty()) {
                    binding.reciclerViewTV.adapter =
                        RatedTVAdapter(dataLst, { vo -> onItemSelected(vo) }) { vo, byteArray -> saveTVImage(vo, byteArray) }
                }
            }
        }
    }

    private fun validateResponseLoadRatedMovie(responseModel: ResponseRatedMovieModel?) {
        responseModel?.let { vo ->
            val ratedTVList = vo.results
            ratedTVList?.let { dataLst ->
                if (dataLst.isNotEmpty()) {
                    binding.reciclerViewMovie.adapter = RatedMovieAdapter(dataLst, { vo -> onItemSelected(vo) }){ vo, byteArray -> saveMovieImage(vo, byteArray) }
                }
            }
        }
    }

    private fun saveTVImage(vo: RatedTVModel, byteArray: ByteArray) {
        profileFragmentViewModel.saveImageTV(vo, byteArray)
    }
    private fun saveMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        profileFragmentViewModel.saveMovieImage(vo, byteArray)
    }

    private fun onItemSelected(vo: Any) {
        val name = when (vo) {
            is RatedTVModel -> vo.name
            is RatedMovieModel -> vo.name
            else -> "Error"
        }
        Log.i(MY_TAG, "onItemSelected: $name}")
    }


    private fun validateResponseLoadProfile(responseModel: ResponseProfileModel?) {
        if (responseModel != null) {
            val name = responseModel.name
            val username = responseModel.username
            val tmdbUrlImage = responseModel.avatar?.tmdb?.avatarPath
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        binding.navHeaderIvUser.setImageBitmap(bitmap)
                        val byteArray = Utils.bitmapToByteArray(bitmap)
                        profileFragmentViewModel.saveImageProfile(responseModel, byteArray)
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }
            tmdbUrlImage?.let {
                val imageUrl = Constants.IMAGE_BASE_URL + tmdbUrlImage
                Picasso.get().load(imageUrl).into(target)
            }
            val byteArray = responseModel.byteArray
            byteArray?.let {
                val bitmap = Utils.byteArrayToBitmap(byteArray)
                binding.navHeaderIvUser.setImageBitmap(bitmap)
            }
            binding.username.text = username
        }
    }
}