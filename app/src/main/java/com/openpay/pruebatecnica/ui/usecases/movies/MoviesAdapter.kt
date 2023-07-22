package com.openpay.pruebatecnica.ui.usecases.movies

import android.graphics.Bitmap
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.databinding.ItemListMovieBinding
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class MoviesAdapter(private val onclickListener: (RatedMovieModel) -> Unit, private val saveMovieImage: (RatedMovieModel, ByteArray) -> Unit) :
    PagingDataAdapter<RatedMovieModel, MoviesAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_movie, parent, false))
    }

//    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.render(item, onclickListener, saveMovieImage) }
    }

    class ViewHolder(private val viewIn: View) : RecyclerView.ViewHolder(viewIn) {
        private val binding = ItemListMovieBinding.bind(viewIn)
        fun render(vo: RatedMovieModel, onclickListener: (RatedMovieModel) -> Unit, saveMovieImage: (RatedMovieModel, ByteArray) -> Unit) {
            binding.name.text = vo.name
//            val shader = LinearGradient(
//                0f, 0f, 0f, binding.overview.lineHeight.toFloat(),
//                intArrayOf(
//                    ContextCompat.getColor(viewIn.context,R.color.black), // Color del texto original
//                    ContextCompat.getColor(viewIn.context,R.color.transparent) // Color transparente
//                ),
//                null,
//                Shader.TileMode.CLAMP
//            )
//            binding.overview.paint.shader = shader
            binding.overview.text = vo.overview
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        binding.itemImageView.setImageBitmap(bitmap)
                        val byteArray = Utils.bitmapToByteArray(bitmap)
                        saveMovieImage(vo, byteArray)
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }
            val tmdbUrlImage = vo.backdropPath
            tmdbUrlImage?.let {
                val imageUrl = Constants.IMAGE_BASE_URL + tmdbUrlImage
                Picasso.get().load(imageUrl).into(target)
            }
            val byteArray = vo.byteArray
            byteArray?.let {
                val bitmap = Utils.byteArrayToBitmap(byteArray)
                binding.itemImageView.setImageBitmap(bitmap)
            }

            binding.itemListLayout.setOnClickListener { onclickListener(vo) }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<RatedMovieModel>() {
        override fun areItemsTheSame(oldItem: RatedMovieModel, newItem: RatedMovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RatedMovieModel, newItem: RatedMovieModel): Boolean {
            return oldItem == newItem
        }
    }

}