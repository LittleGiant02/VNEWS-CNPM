package com.cnpm.vnews.ui.fragment.detail.news_video

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnpm.vnews.databinding.BottomSheetPlayerQualityBinding
import com.cnpm.vnews.ui.adapter.AdapterQuality
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SheetQualityFragment(
    private var listWidth: MutableList<String>,
    private var listHeight: MutableList<String>,
    private var trackSelector: DefaultTrackSelector,
    private var curRes: String

) : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetPlayerQualityBinding

    var onSelected: (String) -> Unit = {}
    var onDismiss: () -> Unit = {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetPlayerQualityBinding.inflate(layoutInflater, container, false)


        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = AdapterQuality(listWidth, listHeight, curRes)
        binding.rvQuality.layoutManager = layoutManager
        binding.rvQuality.adapter = adapter
        adapter.onQualitySelectedListener = { w, h ->
            if (w == "Aut") {
                trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd()
                        .setMaxVideoBitrate(Int.MAX_VALUE)
                )
                onSelected.invoke("Auto")
            } else {
                trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSize(w.toInt(), h.toInt())
                )
                onSelected.invoke("${h}p")
            }
            dismiss()
        }
        binding.layoutCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }


}