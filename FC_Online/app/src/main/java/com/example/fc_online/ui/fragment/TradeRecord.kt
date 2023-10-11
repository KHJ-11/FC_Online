package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fc_online.R
import com.example.fc_online.databinding.FragmentTradeRecordBinding
import com.example.fc_online.ui.adapter.TradeAdapter

class TradeRecord : Fragment() {
    private lateinit var binding: FragmentTradeRecordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentTradeRecordBinding.inflate(layoutInflater)
        val view = binding.root

        bottomNavigationBar()

        return view
    }

    private fun bottomNavigationBar() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.tr_navContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.menuNavigationTr.setupWithNavController(navHostFragment.navController)
        binding.menuNavigationTr.selectedItemId = R.id.tradeRecordBuy
    }

}