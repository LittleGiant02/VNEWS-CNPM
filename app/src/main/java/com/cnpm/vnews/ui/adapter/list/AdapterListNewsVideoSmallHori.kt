package com.cnpm.vnews.ui.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.ItemNewsVideoSmallHoriBinding
import com.cnpm.vnews.model.MediaModel


class AdapterListNewsVideoSmallHori(
    private val size: Int,
    private val listMedia: List<MediaModel>,
    private val isHighlightNews: Boolean,
    private val isHot: Boolean,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemNewsVideoSmallHoriHolder(val binding: ItemNewsVideoSmallHoriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, isHighlightNews: Boolean): ItemNewsVideoSmallHoriHolder {
                val binding = ItemNewsVideoSmallHoriBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                if (isHighlightNews) {
                    val paddingDp = 16
                    val density = binding.root.resources.displayMetrics.density
                    val paddingPixel = (paddingDp * density).toInt()
                    binding.root.setPadding(0, 0, paddingPixel, 0)
                }
                return ItemNewsVideoSmallHoriHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemNewsVideoSmallHoriHolder.from(parent, isHighlightNews)
    }

    var itemClickListener: (MediaModel) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemNewsVideoSmallHoriHolder)
        val media = listMedia[position]

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }

        Glide.with(holder.binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(holder.binding.imageView)

        holder.binding.textViewTitle.text = media.title!!.trim()
        holder.binding.textViewTimeAgo.text =
            getTimeAgo(media.schedule!!, holder.binding.root.resources).trim()

        holder.binding.imageViewIsVideo.visibility =
            if (getValueMetadataForName(media.metadata!!, ContentType) == "0") {
                View.VISIBLE
            } else {
                View.GONE
            }
        holder.binding.textViewHot.apply {
            visibility = if (isHot && media.duration != "" && media.duration != null) {
                text = convertDuration(media.duration)
                holder.binding.imageViewHot.visibility = View.VISIBLE
                View.VISIBLE
            } else {
                holder.binding.imageViewHot.visibility = View.GONE
                View.GONE
            }
        }
    }

    override fun getItemCount(): Int = size
}
