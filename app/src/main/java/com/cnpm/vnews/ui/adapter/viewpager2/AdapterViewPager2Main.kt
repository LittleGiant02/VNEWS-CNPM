package com.cnpm.vnews.ui.adapter.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cnpm.vnews.ui.fragment.category.FragmentCategory
import com.cnpm.vnews.ui.fragment.home.FragmentHome
import com.cnpm.vnews.ui.fragment.live.FragmentLive
import com.cnpm.vnews.ui.fragment.news.FragmentNews
import com.cnpm.vnews.ui.fragment.video.FragmentVideo

class AdapterViewPager2Main(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FragmentHome.newInstance()
            }
            1 -> {
                fragment = FragmentVideo.newInstance()
            }
            2 -> {
                fragment = FragmentNews.newInstance()
            }
            3 -> {
                fragment = FragmentLive.newInstance()
            }
            4 -> {
                fragment = FragmentCategory.newInstance()
            }
        }
        return fragment!!
    }
}