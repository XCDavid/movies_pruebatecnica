package com.openpay.pruebatecnica.ui.usecases.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.data.model.RatedTVModel
import com.openpay.pruebatecnica.databinding.ItemListRatedTvBinding
import com.openpay.pruebatecnica.util.Constants
import com.openpay.pruebatecnica.util.Utils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class RatedTVAdapter(private var items: List<RatedTVModel>, private val onclickListener: (RatedTVModel) -> Unit, private val saveTVImage: (RatedTVModel, ByteArray) -> Unit) :
    RecyclerView.Adapter<RatedTVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_rated_tv, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.render(item, onclickListener, saveTVImage)
    }

    class ViewHolder(private val viewIn: View) : RecyclerView.ViewHolder(viewIn) {
        private val binding = ItemListRatedTvBinding.bind(viewIn)
        fun render(vo: RatedTVModel, onclickListener: (RatedTVModel) -> Unit, saveTVImage: (RatedTVModel, ByteArray) -> Unit) {
            binding.name.text = vo.name
            binding.overview.text = vo.overview
            var xRating = ""
            for (i in 0 until vo.rating) {
                val icon = "*"
                xRating += icon
            }
            binding.rating.text = xRating
            binding.firstAirDate.text = vo.firstAirDate
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        binding.itemImageView.setImageBitmap(bitmap)
                        val byteArray = Utils.bitmapToByteArray(bitmap)
                        saveTVImage(vo, byteArray)
                        //profileFragmentViewModel.saveImageProfile(responseModel,byteArray)
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