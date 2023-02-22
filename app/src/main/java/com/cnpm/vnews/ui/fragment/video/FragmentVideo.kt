package com.cnpm.vnews.ui.fragment.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.cnpm.vnews.const_util.apiVideoScreen
import com.cnpm.vnews.databinding.FragmentVideoBinding
import com.cnpm.vnews.ui.adapter.viewpager2.AdapterViewPager2Video
import com.cnpm.vnews.viewmodel.VideoViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FragmentVideo : Fragment() {
    companion object {
        fun newInstance() = FragmentVideo()
    }

    private lateinit var binding: FragmentVideoBinding
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoViewModel.menuVideoViewModel(apiVideoScreen).observe(viewLifecycleOwner) {
            val listName = mutableListOf<String>()
            val listPrivateKey = mutableListOf<String>()
            it.components!!.forEach { element ->
                listName.add(element.name!!)
                listPrivateKey.add(element.privateKey!!)
            }
            val adapter = AdapterViewPager2Video(this, listPrivateKey)
            binding.viewPager2Video.adapter = adapter
            binding.viewPager2Video.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            TabLayoutMediator(
                binding.tabLayoutVideo,
                binding.viewPager2Video
            ) { tab, pos ->
                tab.text = listName[pos]
            }.attach()
        }
    }
}