package com.cnpm.vnews.ui.fragment.news

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel

class AdapterNews(
    private val listSlider: List<MediaModel>,
    private val listGrid: List<MediaModel>,
    private val listMost1: List<MediaModel>,
    private val itemLarge: List<MediaModel>,
    private val listMost2: List<MediaModel>,
    private val listHighlight2: List<MediaModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val type: Int) {
        LAYOUT_SLIDER(0),
        LAYOUT_GRID(1),
        LAYOUT_MOST1(2),
        LAYOUT_ITEM_LARGE(3),
        LAYOUT_MOST2(4),
        LAYOUT_HIGHLIGHT2(5),
    }

    var itemClickListener: (MediaModel) -> Unit = {}
    var itemSaveClickListener: (DatabaseModel) -> Unit = {}

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.LAYOUT_SLIDER.type
            1 -> ViewType.LAYOUT_GRID.type
            2 -> ViewType.LAYOUT_MOST1.type
            3 -> ViewType.LAYOUT_ITEM_LARGE.type
            4 -> ViewType.LAYOUT_MOST2.type
            5 -> ViewType.LAYOUT_HIGHLIGHT2.type
            else -> ViewType.LAYOUT_HIGHLIGHT2.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LAYOUT_SLIDER.type -> {
                ViewHolderSlider.from(parent)
            }
            ViewType.LAYOUT_GRID.type -> {
                ViewHolderListHori2.from(parent)
            }
            ViewType.LAYOUT_MOST1.type -> {
                ViewHolderListMost.from(parent)
            }
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                ViewHolderItemLarge.from(parent)
            }
            ViewType.LAYOUT_MOST2.type -> {
                ViewHolderListMost.from(parent)
            }
            ViewType.LAYOUT_HIGHLIGHT2.type -> {
                ViewHolderListHighlight2.from(parent)
            }
            else -> {
                ViewHolderListHighlight2.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.LAYOUT_SLIDER.type -> {
                (holder as ViewHolderSlider).bind(listSlider, itemClickListener)
            }
            ViewType.LAYOUT_GRID.type -> {
                (holder as ViewHolderListHori2).bind(listGrid, false, itemClickListener)
            }
            ViewType.LAYOUT_MOST1.type -> {
                (holder as ViewHolderListMost).bind(listMost1, true, itemClickListener, itemSaveClickListener)
            }
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                (holder as ViewHolderItemLarge).bind(itemLarge.first(), itemClickListener)
            }
            ViewType.LAYOUT_MOST2.type -> {
                (holder as ViewHolderListMost).bind(listMost2, true, itemClickListener, itemSaveClickListener)
            }
            ViewType.LAYOUT_HIGHLIGHT2.type -> {
                (holder as ViewHolderListHighlight2).bind(listHighlight2, "Tin nổi bật", itemClickListener, itemSaveClickListener)
            }
        }
    }

    override fun getItemCount() = 6
}
