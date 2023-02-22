package com.cnpm.vnews.ui.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.const_util.optionsLoadImage
import com.cnpm.vnews.databinding.ItemCategorySmallBinding
import com.cnpm.vnews.model.Component

class AdapterGridCategory(private val listComponent: List<Component>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemCategorySmallHolder(val binding: ItemCategorySmallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ItemCategorySmallHolder {
                val binding = ItemCategorySmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemCategorySmallHolder(binding)
            }
        }
    }


    var itemClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemCategorySmallHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemCategorySmallHolder)

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(listComponent[position].privateKey!!)
        }
        Glide.with(holder.binding.root).load(listComponent[position].icon!!)
            .apply(optionsLoadImage)
            .into(holder.binding.imageView)
        holder.binding.textViewNameCategory.text = listComponent[position].name!!
    }

    override fun getItemCount(): Int = listComponent.size
}
