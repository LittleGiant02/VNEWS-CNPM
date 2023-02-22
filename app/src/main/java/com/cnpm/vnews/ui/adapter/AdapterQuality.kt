package com.cnpm.vnews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.databinding.ItemQualityBinding

class AdapterQuality(
    private var listWidth: MutableList<String>,
    private var listHeight: MutableList<String>,
    private var curRes: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onQualitySelectedListener: (String, String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemQualityHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemQualityHolder).bind(getItem(position),getItemW(position), onQualitySelectedListener, curRes)
    }

    override fun getItemCount(): Int = listHeight.size
    private fun getItem(position: Int): String {
        return listHeight[position]
    }
    private fun getItemW(position: Int): String {
        return listWidth[position]
    }
}
class ItemQualityHolder(binding: ItemQualityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val isSelect = binding.isSelect
    private val tvQuality = binding.tvQuallity
    private val tvHD = binding.tvHD
    fun bind(
        height: String,
        width: String, onQualitySelectedListener:(String, String) -> Unit,curRes: String)
    {
        isSelect.visibility = View.INVISIBLE
        tvHD.visibility = View.INVISIBLE
        if(curRes == height)
        {
            isSelect.visibility = View.VISIBLE
        }
        if(height == "1080p")
        {
            tvHD.visibility = View.VISIBLE
        }
        tvQuality.text = height
        itemView.setOnClickListener{
            onQualitySelectedListener.invoke(width.dropLast(1),height.dropLast(1))
        }
    }
    companion object {
        fun from(parent: ViewGroup): ItemQualityHolder {
            val binding =
                ItemQualityBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ItemQualityHolder(binding)
        }
    }

}