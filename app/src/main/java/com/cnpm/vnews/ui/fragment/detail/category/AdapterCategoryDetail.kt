package com.cnpm.vnews.ui.fragment.detail.category

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.vnews.const_util.ViewHolderItemLarge
import com.cnpm.vnews.const_util.ViewHolderListHighlight2
import com.cnpm.vnews.const_util.ViewHolderListHori2
import com.cnpm.vnews.const_util.ViewHolderListMost
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel

class AdapterCategoryDetail(
    private val itemLarge: MediaModel,
    private val listHighlight: List<MediaModel>,
    private val listPlaylist: List<MediaModel>,
    private val listMost: List<MediaModel>,
    private val listHighlightMost: List<MediaModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val type: Int) {
        LAYOUT_ITEM_LARGE(0),
        LIST_HIGHLIGHT(1),
        LIST_PLAYLIST(2),
        LIST_MOST(3),
        LIST_HIGHLIGHT_MOST(4),
    }

    var itemClickListener: (MediaModel) -> Unit = {}
    var itemSaveClickListener: (DatabaseModel) -> Unit = {}


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.LAYOUT_ITEM_LARGE.type
            1 -> ViewType.LIST_HIGHLIGHT.type
            2 -> ViewType.LIST_PLAYLIST.type
            3 -> ViewType.LIST_MOST.type
            4 -> ViewType.LIST_HIGHLIGHT_MOST.type
            else -> ViewType.LIST_HIGHLIGHT_MOST.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                ViewHolderItemLarge.from(parent)
            }
            ViewType.LIST_HIGHLIGHT.type -> {
                ViewHolderListHori2.from(parent)
            }
            ViewType.LIST_PLAYLIST.type -> {
                ViewHolderListHighlight2.from(parent)
            }
            ViewType.LIST_MOST.type -> {
                ViewHolderListMost.from(parent)
            }
            ViewType.LIST_HIGHLIGHT_MOST.type -> {
                ViewHolderListHighlight2.from(parent)
            }
            else -> {
                ViewHolderListMost.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.LAYOUT_ITEM_LARGE.type -> {
                (holder as ViewHolderItemLarge).bind(itemLarge, itemClickListener)
            }
            ViewType.LIST_HIGHLIGHT.type -> {
                (holder as ViewHolderListHori2).bind(listHighlight, false, itemClickListener)
            }
            ViewType.LIST_PLAYLIST.type -> {
                (holder as ViewHolderListHighlight2).bind(listPlaylist, "Playlist", itemClickListener, itemSaveClickListener)
            }
            ViewType.LIST_MOST.type -> {
                (holder as ViewHolderListMost).bind(listMost, true, itemClickListener, itemSaveClickListener)
            }
            ViewType.LIST_HIGHLIGHT_MOST.type -> {
                (holder as ViewHolderListHighlight2).bind(listHighlightMost, "Tin nổi bật", itemClickListener, itemSaveClickListener)
            }
        }
    }

    override fun getItemCount() = 5
}
