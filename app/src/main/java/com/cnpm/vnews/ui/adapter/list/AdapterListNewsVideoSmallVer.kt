package com.cnpm.vnews.ui.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.ItemNewsVideoSmallVerBinding
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel
import java.util.*


class AdapterListNewsVideoSmallVer(private val size: Int, private val listMedia: List<MediaModel>) :
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

    var itemClickListener: (MediaModel) -> Unit = {}
    var itemSaveClickListener: (DatabaseModel) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemNewsVideoSmallVerHolder)
        val media = listMedia[position]

        val title = media.title!!.trim()
        val category = getValueMetadataForName(media.metadata!!, DVBCategories).trim()
        val timeAgo = getTimeAgo(media.schedule!!, holder.binding.root.resources).trim()
        val contentType = getValueMetadataForName(media.metadata, ContentType)

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }

        Glide.with(holder.binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(holder.binding.imageView)

        holder.binding.textViewTitle.text = title
        holder.binding.textViewCategory.text = category

        holder.binding.textViewTimeAgo.text = timeAgo

        holder.binding.imageViewIsVideo.visibility =
            if (contentType == "0") {
                View.VISIBLE
            } else {
                View.GONE
            }
        val slug = getValueMetadataForName(media.metadata, Slug)
        holder.binding.imageViewShare.setOnClickListener {
            shareLink(
                holder.binding.root.context,
                slug,
                media.name!!
            )
        }

        holder.binding.imageViewSave.setOnClickListener {
            val databaseModel = DatabaseModel(
                media.id!!,
                getUrlImageForType(media.image, "Thumbnail")!!,
                title,
                category,
                slug,
                contentType,
                Calendar.getInstance().time
            )
            itemSaveClickListener.invoke(databaseModel)
        }

        if (position == size - 1) {
            holder.binding.view.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = size
}
