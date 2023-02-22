package com.cnpm.vnews.ui.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.ItemNewsVideoSmallVerBinding
import com.cnpm.vnews.model.VideoDetailModel


class AdapterRelated(private val size: Int, private val listMedia: List<VideoDetailModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemNewsVideoSmallVerHolder(val binding: ItemNewsVideoSmallVerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ItemNewsVideoSmallVerHolder {
                val binding = ItemNewsVideoSmallVerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemNewsVideoSmallVerHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemNewsVideoSmallVerHolder.from(parent)
    }

    var itemClickListener: (String) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemNewsVideoSmallVerHolder)
        val media = listMedia[position]
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(media.id!!)
        }

        Glide.with(holder.binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(holder.binding.imageView)

        holder.binding.textViewTitle.text = media.title!!.trim()
        holder.binding.textViewCategory.text =
            getValueMetadataForName(media.metadata!!, DVBCategories).trim()
        holder.binding.textViewTimeAgo.text =
            getTimeAgo(media.schedule!!, holder.binding.root.resources).trim()

        holder.binding.imageViewIsVideo.visibility =
            if (getValueMetadataForName(media.metadata, ContentType) == "0") {
                View.VISIBLE
            } else {
                View.GONE
            }
        holder.binding.imageViewShare.setOnClickListener {
            shareLink(
                holder.binding.root.context,
                getValueMetadataForName(media.metadata, Slug),
                media.title
            )
        }

        holder.binding.imageViewSave.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "Save", Toast.LENGTH_SHORT).show()
        }

        if (position == size - 1) {
            holder.binding.view.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = size
}
