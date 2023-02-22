package com.cnpm.vnews.ui.adapter.viewpager2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cnpm.vnews.ui.fragment.video.FragmentVideoChild

class AdapterViewPager2Video(
    fm: Fragment,
    private val listPrivateKey: MutableList<String>,
) :
    FragmentStateAdapter(fm) {
    override fun getItemCount() = listPrivateKey.size

    override fun createFragment(position: Int): Fragment =
        FragmentVideoChild(listPrivateKey[position])
}