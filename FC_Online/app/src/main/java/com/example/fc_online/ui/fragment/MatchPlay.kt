package com.example.fc_online.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fc_online.R
import com.example.fc_online.data.PlayMatch
import com.example.fc_online.data.TradeType
import com.example.fc_online.databinding.FragmentMatchPlayBinding
import com.example.fc_online.ui.adapter.PlayAdapter
import com.example.fc_online.ui.adapter.TradeAdapter
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import com.example.fc_online.util.SharedViewModel
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchPlay : Fragment() {
    private lateinit var binding: FragmentMatchPlayBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentMatchPlayBinding.inflate(layoutInflater)
        val view = binding.root

        binding.managerMatch.performClick()

        managerMatchClick()
        rankingMathchClick()
        friendlyMatchClick()

        return view
    }

    private fun managerMatchClick() {
        binding.managerMatch.apply {
            setOnClickListener {
                setTextColor(Color.parseColor("#82D8FF"))
                binding.rankingMatch.setTextColor(Color.WHITE)
                binding.friendlyMatch.setTextColor(Color.WHITE)
                userPlayMatch(52)
            }
        }
    }

    private fun rankingMathchClick() {
        binding.rankingMatch.apply {
            setOnClickListener {
                setTextColor(Color.parseColor("#82D8FF"))
                binding.managerMatch.setTextColor(Color.WHITE)
                binding.friendlyMatch.setTextColor(Color.WHITE)
                userPlayMatch(50)
            }
        }
    }

    private fun friendlyMatchClick() {
        binding.friendlyMatch.apply {
            setOnClickListener {
                setTextColor(Color.parseColor("#82D8FF"))
                binding.managerMatch.setTextColor(Color.WHITE)
                binding.rankingMatch.setTextColor(Color.WHITE)
                userPlayMatch(60)
            }
        }
    }

    private fun userPlayMatch(matchNumber: Int) {
        val callGetPlayMatch = api.getPlayMatch("${KEY}","${sharedViewModel.sharedData}",matchNumber,0,10)

        callGetPlayMatch.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val play = response.body()

                if (play?.size() == 0) {
                    binding.nonRecord.visibility = View.VISIBLE
                    binding.rvMatchPlay.visibility = View.GONE
                } else {
                    binding.rvMatchPlay.visibility = View.VISIBLE
                    binding.nonRecord.visibility = View.GONE
                }

                fun viewData(): ArrayList<PlayMatch> {
                    val list = arrayListOf<PlayMatch>()
                    return list.apply {

                        if (play != null) {
                            for (index in 0 until play.size()) {
                                add(PlayMatch(play.get(index).toString()))
                            }
                        }
                    }
                }

                val mPlayAdapter = PlayAdapter(viewData())
                binding.rvMatchPlay.adapter = mPlayAdapter
                binding.rvMatchPlay.layoutManager = LinearLayoutManager(context)

            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("실패", "Error: ${t.message}")
            }

        })
    }


}