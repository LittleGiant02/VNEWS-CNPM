package com.cnpm.vnews.ui.fragment.video

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel

class AdapterVideoChild(
    private val listSlider: List<MediaModel>,
    private val listPlaylist: List<MediaModel>,
    private val listMost: List<MediaModel>,
    private val itemLarge: List<MediaModel>,
    private val listNews: List<MediaModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType(val type: Int) {
        LAYOUT_SLIDER(0),
        LAYOUT_PLAYLIST(1),
        LAYOUT_MOST(2),
        LAYOUT_ITEM_LARGE(3),
        LAYOUT_LIST_NEWEST(4),
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.LAYOUT_SLIDER.type
            1 -> ViewType.LAYOUT_PLAYLIST.type
            2 -> ViewType.LAYOUT_MOST.type
            3 -> ViewType.LAYOUT_ITEM_LARGE.type
            4 -> ViewType.LAYOUT_LIST_NEWEST.type
            else -> ViewType.LAYOUT_LIST_NEWEST.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LAYOUT_SLIDER.type -> {
                ViewHolderSlider.from(parent)
            }
            ViewType.LAYOUT_PLAYLIST.type -> {
                ViewHolderListHori2.from(parent)
            }
            ViewType.LAYOUT_MOST.type -> {
                ViewHolderListMost.from(parent)
            }
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                ViewHolderItemLarge.from(parent)
            }
            ViewType.LAYOUT_LIST_NEWEST.type -> {
                ViewHolderListNewest.from(parent)
            }
            else -> {
                ViewHolderListNewest.from(parent)
            }
        }
    }

    var itemClickListener: (MediaModel) -> Unit = {}
    var itemSaveClickListener: (DatabaseModel) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.LAYOUT_SLIDER.type -> {
                (holder as ViewHolderSlider).bind(listSlider, itemClickListener)
            }
            ViewType.LAYOUT_PLAYLIST.type -> {
                (holder as ViewHolderListHori2).bind(listPlaylist, false, itemClickListener)
            }
            ViewType.LAYOUT_MOST.type -> {
                (holder as ViewHolderListMost).bind(listMost, true, itemClickListener, itemSaveClickListener)
            }
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                (holder as ViewHolderItemLarge).bind(itemLarge.first(), itemClickListener)
            }
            ViewType.LAYOUT_LIST_NEWEST.type -> {
                val newPosition = if (position >= 34) {
                    position - 4
                } else {
                    position
                }
                (holder as ViewHolderListNewest).bind(
                    listNews[newPosition],
                    position,
                    listNews.size + 4,
                    itemClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int = listNews.size + 4
}

