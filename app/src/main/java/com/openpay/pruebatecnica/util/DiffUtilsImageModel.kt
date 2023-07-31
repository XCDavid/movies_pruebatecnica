package com.openpay.pruebatecnica.util

import androidx.recyclerview.widget.DiffUtil
import com.openpay.pruebatecnica.data.model.ImageUploadModel

class DiffUtilsImageModel(
    //private val oldList: List<ImageUploadModel>, private val newList: List<ImageUploadModel>
) : DiffUtil.ItemCallback<ImageUploadModel>() {

    override fun areItemsTheSame(oldItem: ImageUploadModel, newItem: ImageUploadModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageUploadModel, newItem: ImageUploadModel): Boolean {
        return oldItem == newItem
    }
//    override fun getOldListSize(): Int = oldList.size
//
//
//    override fun getNewListSize(): Int = newList.size
//
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition].id == newList[newItemPosition].id
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition] == newList[newItemPosition]
////        return when{
////            oldList[oldItemPosition].id == newList[newItemPosition].id -> true
////            else -> {false}
////        }
//    }
}