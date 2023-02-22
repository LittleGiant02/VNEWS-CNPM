package com.cnpm.vnews.ui.fragment.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnpm.vnews.databinding.FragmentVideoChildBinding
import com.cnpm.vnews.ui.fragment.detail.news_video.FragmentVideoNewsDetail
import com.cnpm.vnews.viewmodel.VideoViewModel

class FragmentVideoChild(private val privateKey: String) : Fragment() {
    private lateinit var binding: FragmentVideoChildBinding
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoChildBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoViewModel.categoryItemViewModel(privateKey).observe(viewLifecycleOwner) {
            val listMedia = it.media!!
            binding.recyclerViewVideoChild.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val listSlider = listMedia.subList(0, 5)     //1
            val listHori2 = listMedia.subList(5, 7)  //2
            val listMost = listMedia.subList(7, 15)    //3
            val itemLarge = listMedia.subList(15, 16)    //4
            val listNews = listMedia.subList(16, listMedia.size)
            val adapter = AdapterVideoChild(listSlider, listHori2, listMost, itemLarge, listNews)
            binding.recyclerViewVideoChild.adapter = adapter
            binding.recyclerViewVideoChild.visibility = View.VISIBLE
            binding.progressBarVideoChild.visibility = View.GONE

            adapter.itemClickListener = { news ->
                FragmentVideoNewsDetail.openWith(
                    requireActivity().supportFragmentManager,
                    news.id!!
                )
            }
        }
    }

    companion object {
        fun newInstance(privateKey: String) = FragmentVideoChild(privateKey)
    }
}