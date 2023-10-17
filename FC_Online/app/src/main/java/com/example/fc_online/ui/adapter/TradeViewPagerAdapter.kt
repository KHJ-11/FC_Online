package com.example.fc_online.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fc_online.ui.fragment.TradeRecordBuy
import com.example.fc_online.ui.fragment.TradeRecordSell
import com.example.fc_online.ui.fragment.TradeRecordStatistics

private const val NUM_TABS = 3
class TradeViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TradeRecordBuy()
            1 -> return TradeRecordSell()
            2 -> return TradeRecordStatistics()
        }
        return TradeRecordStatistics()
    }


}