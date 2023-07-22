package com.openpay.pruebatecnica.ui.usecases.movies

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.databinding.ItemListMovieBinding
import com.openpay.pruebatecnica.ui.usecases.profile.RatedMovieAdapter
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class TrendingMoviesAdapter(private var items: List<RatedMovieModel>,private val onclickListener: (RatedMovieModel) -> Unit, private val saveMovieImage: (RatedMovieModel, ByteArray) -> Unit) :
    RecyclerView.Adapter<TrendingMoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_movie, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.render(item, onclickListener, saveMovieImage)
    }

    class ViewHolder(private val viewIn: View) : RecyclerView.ViewHolder(viewIn) {
        private val binding = ItemListMovieBinding.bind(viewIn)
        fun render(vo: RatedMovieModel, onclickListener: (RatedMovieModel) -> Unit, saveMovieImage: (RatedMovieModel, ByteArray) -> Unit) {
            binding.name.text = vo.name
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

}