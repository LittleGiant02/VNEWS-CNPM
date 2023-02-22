package com.cnpm.vnews.ui.fragment.live

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.const_util.ViewHolderCategoryGrid2
import com.cnpm.vnews.const_util.ViewHolderListHighlight2
import com.cnpm.vnews.model.Component
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel

class AdapterLive(
    private val listHot: List<MediaModel>,
    private val listProgram: List<Component>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val type: Int) {
        LAYOUT_HOT(0),
        LAYOUT_PROGRAM(1),
    }

    var itemHotClickListener: (MediaModel) -> Unit = {}
    var itemSaveClickListener: (DatabaseModel) -> Unit = {}
    var itemProgramClickListener: (String) -> Unit = {}

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.LAYOUT_HOT.type
            1 -> ViewType.LAYOUT_PROGRAM.type
            else -> ViewType.LAYOUT_PROGRAM.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LAYOUT_HOT.type -> {
                ViewHolderListHighlight2.from(parent)
            }
            ViewType.LAYOUT_PROGRAM.type -> {
                ViewHolderCategoryGrid2.from(parent)
            }
            else -> {
                ViewHolderCategoryGrid2.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.LAYOUT_HOT.type -> {
                (holder as ViewHolderListHighlight2).bind(listHot, "Đừng bỏ lỡ", itemHotClickListener, itemSaveClickListener)
            }
            ViewType.LAYOUT_PROGRAM.type -> {
                (holder as ViewHolderCategoryGrid2).bind(listProgram, itemProgramClickListener)
            }
        }
    }

    override fun getItemCount() = 2
}
