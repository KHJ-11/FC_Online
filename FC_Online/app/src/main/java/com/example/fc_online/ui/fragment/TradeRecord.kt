package com.example.fc_online.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.databinding.FragmentTradeRecordBinding
import com.example.fc_online.ui.adapter.TradeViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TradeRecord : Fragment() {
    private lateinit var binding: FragmentTradeRecordBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val tabTitleArray = arrayOf(
        "구매","판매","통계"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentTradeRecordBinding.inflate(layoutInflater)
        val view = binding.root

        viewPager()

        return view
    }

    private fun viewPager() {
        binding.viewPager.adapter = TradeViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

}