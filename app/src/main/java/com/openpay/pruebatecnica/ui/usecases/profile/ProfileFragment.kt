package com.openpay.pruebatecnica.ui.usecases.profile

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.data.model.ResponseProfileModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedTVModel
import com.openpay.pruebatecnica.databinding.FragmentProfileBinding
import com.openpay.pruebatecnica.ui.common.RecyclerViewSwipeTouchListener
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

    private lateinit var gestureDetector: GestureDetector
    private var isAnimating = false
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

        binding.linearLayoutTitles.setOnClickListener {
            if (binding.reciclerViewTV.visibility == View.VISIBLE) {
                animateSwap(binding.titleRatedTV, binding.titleRatedMovie)
                animateVisibilityChange(binding.reciclerViewTV, binding.reciclerViewMovie)

            } else {
                animateSwap(binding.titleRatedMovie, binding.titleRatedTV)
                animateVisibilityChange(binding.reciclerViewMovie, binding.reciclerViewTV)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el GestureDetector
        gestureDetector = GestureDetector(requireContext(), GestureListener())

        // Asignar el listener de touch a la vista principal del fragment
        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun validateResponseLoadRatedTV(responseModel: ResponseRatedTVModel?) {
        responseModel?.let { responseModel ->
            val ratedTVList = responseModel.results
            ratedTVList?.let { dataLst ->
                if (dataLst.isNotEmpty()) {
                    binding.reciclerViewTV.adapter =
                        RatedTVAdapter(dataLst, { vo -> onItemSelected(vo) }) { vo, byteArray -> saveTVImage(vo, byteArray) }
                    RecyclerViewSwipeTouchListener(requireContext(), binding.reciclerViewTV) { dir -> performSwipeAnimation(dir) }
                }
            }
        }
    }

    private fun validateResponseLoadRatedMovie(responseModel: ResponseRatedMovieModel?) {
        responseModel?.let { vo ->
            val ratedTVList = vo.results
            ratedTVList?.let { dataLst ->
                if (dataLst.isNotEmpty()) {
                    binding.reciclerViewMovie.adapter =
                        RatedMovieAdapter(dataLst, { vo -> onItemSelected(vo) }) { vo, byteArray -> saveMovieImage(vo, byteArray) }
                    RecyclerViewSwipeTouchListener(requireContext(), binding.reciclerViewMovie) { dir -> performSwipeAnimation(dir) }
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

    private fun animateSwap(view1: TextView, view2: TextView) {
        if (isAnimating) {
            return // Si la animación ya está en progreso, no hacer nada
        }
        isAnimating = true
        val animDuration = 500L // Duración de la animación en milisegundos

        val originalX1 = view1.x
        val originalX2 = view2.x

        val originalTextSize1 = (view1).textSize
        val originalTextSize2 = (view2).textSize
        val animGravity1 = ValueAnimator.ofFloat(0f, 1f)
//        val animGravity2 = ValueAnimator.ofFloat(0f, 1f)
        var textSizeView1: Float
        var textSizeView2: Float
        if (originalTextSize1 > originalTextSize2) {
            textSizeView1 = resources.getDimension(R.dimen.inactive_title_profile) / resources.displayMetrics.scaledDensity
            textSizeView2 = resources.getDimension(R.dimen.active_title_profile) / resources.displayMetrics.scaledDensity
            animGravity1.addUpdateListener { valueAnimator ->
                view1.gravity = Gravity.CENTER_VERTICAL or Gravity.START
                view2.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
            }
//            animGravity2.addUpdateListener { valueAnimator ->
//                view1.gravity = Gravity.CENTER_VERTICAL
//                view2.gravity = Gravity.CENTER_VERTICAL or Gravity.START
//            }
        } else {
            textSizeView1 = resources.getDimension(R.dimen.active_title_profile) / resources.displayMetrics.scaledDensity
            textSizeView2 = resources.getDimension(R.dimen.inactive_title_profile) / resources.displayMetrics.scaledDensity
            animGravity1.addUpdateListener { valueAnimator ->
                view1.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                view2.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            }
        }

        val anim1 = ObjectAnimator.ofFloat(view1, "x", originalX2)
        anim1.duration = animDuration

        val anim2 = ObjectAnimator.ofFloat(view2, "x", originalX1)
        anim2.duration = animDuration

        val textAnim1 = ObjectAnimator.ofFloat(view1, "textSize", textSizeView1)
        textAnim1.duration = animDuration

        val textAnim2 = ObjectAnimator.ofFloat(view2, "textSize", textSizeView2)
        textAnim2.duration = animDuration

//
//        val alignmentAnimator = ObjectAnimator.ofFloat(view1, "gravity", (Gravity.CENTER_VERTICAL or Gravity.START))
//        alignmentAnimator.duration = animDuration
//

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2, textAnim1, textAnim2, animGravity1)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isAnimating = false // Marcar la animación como finalizada
            }
        })
        animatorSet.start()


    }

    private fun animateVisibilityChange(recyclerView1: RecyclerView, recyclerView2: RecyclerView) {
        // Cambiar la visibilidad de las RecyclerViews de manera instantánea
        val isVisible1 = recyclerView1.visibility == View.VISIBLE
        recyclerView1.visibility = if (isVisible1) View.GONE else View.VISIBLE

        val isVisible2 = recyclerView2.visibility == View.VISIBLE
        recyclerView2.visibility = if (isVisible2) View.GONE else View.VISIBLE

        // Aplicar animación de desvanecimiento
        val alphaAnimation1 = AlphaAnimation(if (isVisible1) 1f else 0f, if (isVisible1) 0f else 1f)
        alphaAnimation1.duration = 500L
        recyclerView1.startAnimation(alphaAnimation1)

        val alphaAnimation2 = AlphaAnimation(if (isVisible2) 1f else 0f, if (isVisible2) 0f else 1f)
        alphaAnimation2.duration = 500L
        recyclerView2.startAnimation(alphaAnimation2)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
            val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                // El gesto fue un deslizamiento horizontal
                if (diffX > 0) {
                    // Swipe hacia la derecha
                    // Realizar la animación para el swipe hacia la derecha
                    performSwipeAnimation(Constants.SwipeDirection.RIGHT)
                } else {
                    // Swipe hacia la izquierda
                    // Realizar la animación para el swipe hacia la izquierda
                    performSwipeAnimation(Constants.SwipeDirection.LEFT)
                }
                return true
            }
            return false
        }
    }



    private fun performSwipeAnimation(direction: Constants.SwipeDirection?) {
        // Aquí puedes agregar tu lógica para realizar la animación deseada
        if (binding.reciclerViewTV.visibility == View.VISIBLE) {
            animateSwap(binding.titleRatedTV, binding.titleRatedMovie)
            animateVisibilityChange(binding.reciclerViewTV, binding.reciclerViewMovie)

        } else {
            animateSwap(binding.titleRatedMovie, binding.titleRatedTV)
            animateVisibilityChange(binding.reciclerViewMovie, binding.reciclerViewTV)
        }
        if (direction == Constants.SwipeDirection.RIGHT) {

        }else{

        }
    }
}