package com.example.fc_online.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fc_online.R
import com.example.fc_online.data.TradeType
import com.example.fc_online.databinding.FragmentTradeRecordBuyBinding
import com.example.fc_online.ui.adapter.TradeAdapter
import com.example.fc_online.util.Constants
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import com.example.fc_online.util.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeRecordBuy : Fragment() {
    private lateinit var binding: FragmentTradeRecordBuyBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentTradeRecordBuyBinding.inflate(layoutInflater)
        val view = binding.root

        userBuyTrade()

//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                getTrade().body().let {
//                    setAdapter(it as ArrayList<TradeType>)
//                }
//            } catch (e: Exception) {
//
//            }
//        }

//        fetchData()

//        test()

        return view
    }

//    private fun test() {
//        binding.progressBar.visibility = View.VISIBLE

//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                getTrade().body().let {
//                    setAdapter(it as ArrayList<TradeType>)
//                    delay(3000)
//                    binding.progressBar.visibility = View.GONE
//                }
//
//            } catch (e: Exception) {
//                binding.progressBar.visibility = View.GONE
//            }
//        }

//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                delay(3000)
//                withContext(Dispatchers.Main) {
//                    binding.progressBar.visibility = View.GONE
//                    getTrade().body().let {
//                        setAdapter(it as ArrayList<TradeType>)
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    binding.progressBar.visibility = View.GONE
//                }
//            }
//        }
//    }

//    private fun fetchData() {
//        binding.progressBar.visibility = ProgressBar.VISIBLE
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                getTrade().body().let {
//                    setAdapter(it as ArrayList<TradeType>)
//                }
//                launch(Dispatchers.Main) {
//                    binding.progressBar.visibility = ProgressBar.GONE
//                }
//            } catch (e: Exception) {
//                launch(Dispatchers.Main) {
//                    binding.progressBar.visibility = ProgressBar.GONE
//                }
//            }
//        }
//    }

//    private suspend fun getTrade(): Response<List<TradeType>> {
//        return api.getTradeType2("${KEY}","${sharedViewModel.sharedData}","buy",1,10)
//    }

    private fun userBuyTrade() {
        val callGetTradeType = api.getTradeType("${KEY}","${sharedViewModel.sharedData}","buy",1,10)

        callGetTradeType.enqueue(object : Callback<List<TradeType>> {
            override fun onResponse(call: Call<List<TradeType>>, response: Response<List<TradeType>>) {
                val buy = response.body()
                buy?.let {
                    setAdapter(it as ArrayList<TradeType>)
                }
            }

            override fun onFailure(call: Call<List<TradeType>>, t: Throwable) {
                Log.e("실패", "Error: ${t.message}")
            }

        })
    }

    private fun setAdapter(tradeList: ArrayList<TradeType>) {
        val mTradeAdapter = TradeAdapter(tradeList)
        binding.rvBuyRecord.apply {
            adapter = mTradeAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
        }
    }
}