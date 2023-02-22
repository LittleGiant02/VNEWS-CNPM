package com.cnpm.vnews.const_util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cnpm.vnews.databinding.*
import com.cnpm.vnews.model.Component
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.room_db.DatabaseModel
import com.cnpm.vnews.ui.adapter.list.AdapterGridCategory
import com.cnpm.vnews.ui.adapter.list.AdapterListNewsVideoSmallHori
import com.cnpm.vnews.ui.adapter.list.AdapterListNewsVideoSmallVer
import com.cnpm.vnews.ui.adapter.list.AdapterListVideoLarge
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import java.util.*


class ViewHolderListHighlight2(
    binding: WidgetListHori2Binding,
) :
    RecyclerView.ViewHolder(binding.root) {
    val context = binding.root
    val view2 = binding.view2
    val view = binding.view
    private val recyclerView = binding.recyclerView
    private val textViewTitleCategory = binding.textViewTitleCategory
    fun bind(
        listVideo: List<MediaModel>,
        titleCategory: String,
        itemClickListener: (MediaModel) -> Unit,
        itemSaveClickListener: (DatabaseModel) -> Unit,
    ) {
        val adapter = AdapterListNewsVideoSmallHori(
            listVideo.size,
            listVideo,
            true,
            titleCategory == "Đừng bỏ lỡ"
        )

        adapter.itemClickListener = {
            itemClickListener.invoke(it)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        view2.visibility = View.VISIBLE
        if (titleCategory == "Đừng bỏ lỡ") {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
        textViewTitleCategory.text = titleCategory
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): ViewHolderListHighlight2 {
            val binding =
                WidgetListHori2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderListHighlight2(binding)
        }
    }
}


class ViewHolderItemLargeHighlight(private val binding: ItemNewsVideoHighlightLargeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        media: MediaModel, isHighlight: Boolean, itemClickListener: (MediaModel) -> Unit,
        itemSaveClickListener: (DatabaseModel) -> Unit,
    ) {
        val contentType = getValueMetadataForName(media.metadata!!, ContentType)
        val category = getValueMetadataForName(media.metadata, DVBCategories).trim()
        val title = media.title!!.trim()
        Glide.with(binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(binding.imageView)

        binding.view.visibility = if (isHighlight) {
            View.VISIBLE
        } else {
            View.GONE
        }
        itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }
        binding.textViewTitle.text = title
        binding.textViewCategory.text = category

        binding.textViewTimeAgo.text =
            getTimeAgo(media.schedule!!, binding.root.resources).trim()

        binding.imageViewIsVideo.visibility =
            if (contentType == "0") {
                View.VISIBLE
            } else {
                View.GONE
            }
        val slug = getValueMetadataForName(media.metadata, Slug)
        binding.imageViewShare.setOnClickListener {
            shareLink(
                binding.root.context,
                slug,
                media.name!!
            )
        }
        binding.imageViewSave.setOnClickListener {
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
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderItemLargeHighlight {
            val binding =
                ItemNewsVideoHighlightLargeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderItemLargeHighlight(binding)
        }
    }
}


class ViewHolderAnotherHome(private val binding: WidgetAnotherHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        media: MediaModel,
        name: String,
        privateKey: String,
        itemClickListener: (MediaModel) -> Unit,
        itemMoreClickListener: (String) -> Unit,
        itemSaveClickListener: (DatabaseModel) -> Unit,
    ) {
        binding.layoutCategory.setOnClickListener {
            itemMoreClickListener.invoke(privateKey)
        }
        binding.sectionLayoutItem.view.visibility = View.GONE
        binding.textViewTitleCategory.text = name
        binding.sectionLayoutItem.itemNewsVideoLarge.setOnClickListener {
            itemClickListener.invoke(media)
        }
        Glide.with(binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(binding.sectionLayoutItem.imageView)

        binding.sectionLayoutItem.textViewTitle.text = media.title!!.trim()
        binding.sectionLayoutItem.textViewCategory.text = name
//            getValueMetadataForName(media.metadata!!, DVBCategories).trim()
        binding.sectionLayoutItem.textViewTimeAgo.text =
            getTimeAgo(media.schedule!!, binding.root.resources).trim()

        binding.sectionLayoutItem.imageViewIsVideo.visibility =
            if (getValueMetadataForName(media.metadata!!, ContentType) == "0") {
                View.VISIBLE
            } else {
                View.GONE
            }
        binding.sectionLayoutItem.imageViewShare.setOnClickListener {
            shareLink(
                binding.root.context,
                getValueMetadataForName(media.metadata, Slug),
                media.name!!
            )
        }

        binding.sectionLayoutItem.imageViewSave.setOnClickListener {
            Toast.makeText(binding.root.context, "Save", Toast.LENGTH_SHORT).show()
        }
//        binding.view.visibility = View.GONE
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderAnotherHome {
            val binding =
                WidgetAnotherHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderAnotherHome(binding)
        }
    }
}


class ViewHolderSlider(binding: WidgetSliderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val viewPager2Slider = binding.viewPager2Slider
    private val tabLayoutDotSlider = binding.tabLayoutDotSlider
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null
    fun bind(listVideo: List<MediaModel>, itemClickListener: (MediaModel) -> Unit) {
        val adapter = AdapterListVideoLarge(true, listVideo.size, listVideo)
        viewPager2Slider.adapter = adapter
        adapter.itemClickListener = {
            itemClickListener.invoke(it)
        }
        TabLayoutMediator(
            tabLayoutDotSlider,
            viewPager2Slider
        ) { _: TabLayout.Tab, _: Int ->//tab, index
        }.attach()
        job?.cancel()
        job = scope.launch {
            while (true) {
                val curPos = viewPager2Slider.currentItem
                if (curPos == listVideo.size - 1) {
                    viewPager2Slider.currentItem = 0
                } else {
                    viewPager2Slider.setCurrentItem(curPos + 1, true)
                }
                delay(TIMER_DELAY_MILLISECONDS_SLIDER)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderSlider {
            val binding =
                WidgetSliderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderSlider(binding)
        }
    }
}

class ViewHolderListHori2(
    binding: WidgetListHori2Binding,
) :
    RecyclerView.ViewHolder(binding.root) {
    private val recyclerView = binding.recyclerView
    private val view2 = binding.view2
    val view = binding.view
    private val textViewTitleCategory = binding.textViewTitleCategory

    fun bind(
        listVideo: List<MediaModel>,
        isVisibleTitleCategory: Boolean,
        itemClickListener: (MediaModel) -> Unit
    ) {
        val adapter = AdapterListNewsVideoSmallHori(listVideo.size, listVideo, false, false)
        adapter.itemClickListener = {
            itemClickListener.invoke(it)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(recyclerView.context, 2)
        view.visibility = View.VISIBLE
        view2.visibility = View.INVISIBLE
        textViewTitleCategory.visibility = if (isVisibleTitleCategory) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): ViewHolderListHori2 {
            val binding =
                WidgetListHori2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderListHori2(binding)
        }
    }
}

class ViewHolderListMost(
    binding: WidgetListNewsBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    private val recyclerView = binding.recyclerView
    private val textViewTitleCategory = binding.textViewTitleCategory

    fun bind(
        listVideo: List<MediaModel>,
        isVisibleTitleCategory: Boolean,
        itemClickListener: (MediaModel) -> Unit,
        itemSaveClickListener: (DatabaseModel) -> Unit,
    ) {
        val adapter = AdapterListNewsVideoSmallVer(listVideo.size, listVideo)
        adapter.itemClickListener = {
            itemClickListener.invoke(it)
        }

        adapter.itemSaveClickListener = {
            itemSaveClickListener.invoke(it)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        textViewTitleCategory.visibility = if (isVisibleTitleCategory) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderListMost {
            val binding =
                WidgetListNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderListMost(binding)
        }
    }
}

class ViewHolderItemLarge(
    private val binding: ItemNewsVideoLargeBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(media: MediaModel, itemClickListener: (MediaModel) -> Unit) {
        binding.itemNewsVideoLarge.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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

        itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderItemLarge {
            val binding =
                ItemNewsVideoLargeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderItemLarge(binding)
        }
    }
}

class ViewHolderListNewest(
    private val binding: ItemNewsVideoSmallVerBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(media: MediaModel, position: Int, size: Int, itemClickListener: (MediaModel) -> Unit) {
        Glide.with(binding.root).load(getUrlImageForType(media.image!!, "lg"))
            .apply(optionsLoadImage)
            .into(binding.imageView)

        binding.textViewTitle.text = media.title!!.trim()
        binding.textViewCategory.text =
            getValueMetadataForName(media.metadata!!, DVBCategories).trim()
        binding.textViewTimeAgo.text = getTimeAgo(media.schedule!!, binding.root.resources).trim()

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

        itemView.setOnClickListener {
            itemClickListener.invoke(media)
        }

        binding.imageViewSave.setOnClickListener {
            Toast.makeText(binding.root.context, "Save", Toast.LENGTH_SHORT).show()
        }

        val paddingDp = 16
        val density = binding.root.resources.displayMetrics.density
        val paddingPixel = (paddingDp * density).toInt()
        binding.root.setPadding(paddingPixel, paddingPixel, paddingPixel, 0)
        if (position == size - 1) {
            binding.view.visibility = View.GONE
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderListNewest {
            val binding =
                ItemNewsVideoSmallVerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderListNewest(binding)
        }
    }
}

class ViewHolderCategoryGrid2(
    binding: FragmentCategoryBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    private val recyclerView = binding.recyclerViewCategoryProgram
    private val progressBar = binding.progressBarCategory

    fun bind(listComponent: List<Component>, itemClickListener: (String) -> Unit) {
        progressBar.visibility = View.GONE
        val layoutManager = GridLayoutManager(
            recyclerView.context,
            2
        )
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterGridCategory(
            listComponent
        )
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
        adapter.itemClickListener = {
            itemClickListener.invoke(it)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): ViewHolderCategoryGrid2 {
            val binding =
                FragmentCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ViewHolderCategoryGrid2(binding)
        }
    }
}

