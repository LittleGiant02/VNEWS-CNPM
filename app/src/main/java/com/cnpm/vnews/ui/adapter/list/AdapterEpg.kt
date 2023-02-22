package com.cnpm.vnews.ui.adapter.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.R
import com.cnpm.vnews.const_util.getPositionLive
import com.cnpm.vnews.databinding.ItemEpgBinding
import com.cnpm.vnews.model.EPGModel

class AdapterEpg(private val listEpg: List<EPGModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemEpgViewHolder(val binding: ItemEpgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ItemEpgViewHolder {
                val binding = ItemEpgBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemEpgViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemEpgViewHolder.from(parent)
    }

    var itemClickListener: (EPGModel, Int) -> Unit = {_, _ ->  }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemEpgViewHolder)
        val epgItem = listEpg[position]


        holder.binding.textViewTime.text = "${epgItem.startTime!!.substring(11, 16)} - ${epgItem.endTime!!.substring(11, 16)}"
        holder.binding.textViewName.text = epgItem.name!!.trim()

        holder.binding.textViewDescription.apply {
            if (epgItem.description != null && epgItem.description != "") {
                text = "epgItem.description"
                View.VISIBLE
            }
        else {
                View.GONE
            }
        }

        if(positionLive < position){
            holder.binding.textViewTime.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTimeAgo))
            holder.binding.textViewName.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTimeAgo))
            holder.binding.textViewDescription.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTimeAgo))
        } else if(positionLive == position){
            holder.binding.imageViewIsLive.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                itemClickListener.invoke(epgItem, position)
            }
        } else {
            holder.binding.textViewTime.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTitle))
            holder.binding.textViewName.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTitle))
            holder.binding.textViewDescription.setTextColor(ContextCompat.getColor(holder.binding.textViewTime.context, R.color.kColorTitle))
            holder.itemView.setOnClickListener {
                itemClickListener.invoke(epgItem, position)
            }
        }
    }

    val positionLive = getPositionLive(listEpg)

    override fun getItemCount(): Int = listEpg.size
}