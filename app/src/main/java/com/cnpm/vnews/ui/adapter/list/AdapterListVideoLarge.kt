package com.cnpm.vnews.ui.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.ItemNewsVideoLargeBinding
import com.cnpm.vnews.model.MediaModel


class AdapterListVideoLarge(
    private val isSlider: Boolean,
    private val size: Int,
    private val listMedia: List<MediaModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemVideoLargeHolder(val binding: ItemNewsVideoLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ItemVideoLargeHolder {
                val binding = ItemNewsVideoLargeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemVideoLargeHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemVideoLargeHolder.from(parent)
    }

    var itemClickListener: (MediaModel) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemVideoLargeHolder)
        val media = listMedia[position]
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }

        holder.apply {
            if (isSlider) {
                binding.itemNewsVideoLarge.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            Glide.with(binding.root).load(getUrlImageForType(media.image!!, "lg"))
                .apply(optionsLoadImage)
                .into(binding.imageView)

            binding.textViewTitle.text = media.title!!.trim()
            binding.textViewCategory.text =
                getValueMetadataForName(media.metadata!!, DVBCategories).trim()
            binding.textViewTimeAgo.text =
                getTimeAgo(media.schedule!!, binding.root.resources).trim()

            binding.imageViewIsVideo.visibility =
                if (getValueMetadataForName(media.metadata, ContentType) == "0") {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            binding.imageViewShare.setOnClickListener {
                shareLink(
                    binding.root.context,
                    getValueMetadataForName(media.metadata, Slug),
                    media.name!!
                )
            }

            binding.imageViewSave.setOnClickListener {
                Toast.makeText(binding.root.context, "Save", Toast.LENGTH_SHORT).show()
            }
            binding.view.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = size
}
