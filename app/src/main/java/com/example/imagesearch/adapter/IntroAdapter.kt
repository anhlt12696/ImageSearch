package com.example.imagesearch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.imagesearch.fragment.IntroOneFragment
import com.example.imagesearch.fragment.IntroThreeFragment
import com.example.imagesearch.fragment.IntroTwoFragment

class IntroAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> IntroOneFragment()
            1 -> IntroTwoFragment()
            else -> IntroThreeFragment()
        }
    }
}