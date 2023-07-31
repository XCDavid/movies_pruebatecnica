package com.openpay.pruebatecnica.ui.usecases.upload

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.data.model.ImageUploadModel
import com.openpay.pruebatecnica.databinding.ItemListUploadImageBinding
import com.openpay.pruebatecnica.util.DiffUtilsImageModel
import com.openpay.pruebatecnica.util.Utils


class ImageUploadAdapter() :
    ListAdapter<ImageUploadModel, ImageUploadAdapter.ViewHolder>(DiffUtilsImageModel()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_upload_image, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.render(item, onclickListener)
    }

    class ViewHolder(private val viewIn: View) : RecyclerView.ViewHolder(viewIn) {
        private val binding = ItemListUploadImageBinding.bind(viewIn)
        fun render(vo: ImageUploadModel, onclickListener: (ImageUploadModel) -> Unit) {
            binding.name.text = vo.name
            val bitmapIamge = Utils.getBitmapFromUri(viewIn.context.contentResolver, vo.uri)
            binding.itemImageView.setImageBitmap(bitmapIamge)
            binding.progress.text = vo.progress.toString()
            if (vo.progress == 100) binding.checkCompleteUpload.visibility = View.VISIBLE
            else binding.checkCompleteUpload.visibility = View.INVISIBLE

            binding.itemListLayout.setOnClickListener { onclickListener(vo) }
        }
    }

    fun updateList(newLst: List<ImageUploadModel>) {
        val diff = DiffUtilsImageModel(items, newLst)
        val result = DiffUtil.calculateDiff(diff)
        items = newLst
        result.dispatchUpdatesTo(this)
    }

    fun updateProgress(itemId: Int, progress: Int) {
        val itemIndex = items.indexOfFirst { it.id == itemId }
        if (itemIndex != -1) {
            items[itemIndex].progress = progress
            notifyItemChanged(itemIndex)

            if (progress == 100) {
                val itemToRemove = items[itemIndex]
                removeElementAfterDelay(itemToRemove, 500) // Aquí defines el tiempo de retardo deseado en milisegundos
            }
        }
    }

    private fun removeElementAfterDelay(element: ImageUploadModel, delayMillis: Long) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.postDelayed({
            val newItems = items.toMutableList().apply {
                remove(element)

            }
            updateList(newItems.toList())
        }, delayMillis)
    }
}