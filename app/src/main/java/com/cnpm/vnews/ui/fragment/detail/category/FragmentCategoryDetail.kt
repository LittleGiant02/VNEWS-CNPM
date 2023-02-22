package com.cnpm.vnews.ui.fragment.detail.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnpm.vnews.R
import com.cnpm.vnews.const_util.backPressDispatcher
import com.cnpm.vnews.databinding.FragmentCategoryDetailBinding
import com.cnpm.vnews.ui.fragment.detail.news_video.FragmentVideoNewsDetail
import com.cnpm.vnews.viewmodel.VideoViewModel

class FragmentCategoryDetail(private val privateKey: String) : Fragment() {
    companion object {
        private fun newInstance(privateKey: String) = FragmentCategoryDetail(privateKey)
        fun openWith(fragmentManager: FragmentManager, id: String) {
            fragmentManager.beginTransaction()
                .replace(
                    R.id.frame_child,
                    newInstance(id), "news"
                )
                .addToBackStack(null).commit()
        }
    }

    private lateinit var binding: FragmentCategoryDetailBinding
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.e("fragment id", "$id")
        binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back()

        videoViewModel.menuVideoViewModel(privateKey).observe(viewLifecycleOwner){
            val itemLarge = it.media!!.first()
            val listHighlight = it.media.subList(1, 7)
            val listPlaylist = it.media.subList(7, 12)
            val listMost = it.media.subList(12, 17)
            val listHighlightMost = it.media.subList(17, 22)

            binding.sectionAppBar.textViewTitle.text = it.name!!
            val adapter = AdapterCategoryDetail(
                itemLarge,
                listHighlight,
                listPlaylist,
                listMost,
                listHighlightMost,
            )
            binding.recyclerViewCategoryDetail.layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            binding.recyclerViewCategoryDetail.adapter = adapter
            binding.layoutProgressbar.visibility = View.GONE
            binding.recyclerViewCategoryDetail.visibility = View.VISIBLE
            adapter.itemClickListener = { news ->
                FragmentVideoNewsDetail.openWith(
                    requireActivity().supportFragmentManager,
                    news.id!!
                )
            }
        }


    }

    private fun back() {
        binding.sectionAppBar.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        //back press
        backPressDispatcher(requireActivity(), viewLifecycleOwner)
    }



}