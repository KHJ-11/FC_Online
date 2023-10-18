package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fc_online.R
import com.example.fc_online.data.TradeType
import com.example.fc_online.databinding.FragmentTradeRecordSellBinding
import com.example.fc_online.ui.adapter.TradeAdapter
import com.example.fc_online.util.Constants
import com.example.fc_online.util.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeRecordSell : Fragment() {
    private lateinit var binding: FragmentTradeRecordSellBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentTradeRecordSellBinding.inflate(layoutInflater)
        val view = binding.root

        userSellTrade()

        return view
    }

    private fun userSellTrade() {
        val callGetTradeType = Constants.api.getTradeType("${Constants.KEY}","${sharedViewModel.sharedData}","sell",1,10)

        callGetTradeType.enqueue(object : Callback<List<TradeType>> {
            override fun onResponse(call: Call<List<TradeType>>, response: Response<List<TradeType>>) {
                val buy = response.body()
                buy?.let {
                    setAdapter(it as ArrayList<TradeType>)
                }
            }

            override fun onFailure(call: Call<List<TradeType>>, t: Throwable) {
            }

        })
    }

    private fun setAdapter(tradeList: ArrayList<TradeType>) {
        val mTradeAdapter = TradeAdapter(tradeList)
        binding.rvSellRecord.apply {
            adapter = mTradeAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
        }
    }

}