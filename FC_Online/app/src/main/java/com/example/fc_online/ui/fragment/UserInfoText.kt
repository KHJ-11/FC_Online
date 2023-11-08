package com.example.fc_online.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fc_online.MainActivity
import com.example.fc_online.R
import com.example.fc_online.data.PlayMatch
import com.example.fc_online.data.SaveName
import com.example.fc_online.data.UserRanked
import com.example.fc_online.data.match.MatchValues
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.databinding.FragmentUserInfoTextBinding
import com.example.fc_online.ui.adapter.RankedAdapter
import com.example.fc_online.ui.adapter.SaveNameAdapter
import com.example.fc_online.util.Constants
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import com.example.fc_online.util.DataRepository
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoText : Fragment() {
    private lateinit var binding: FragmentUserInfoTextBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dataRepository: DataRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentUserInfoTextBinding.inflate(layoutInflater)
        val view = binding.root

        userInfoText()

        binding.saveButton.setOnClickListener {
            saveData()
        }
        (activity as MainActivity)

        userRankedText()
        fieldSquad()

        return view
    }

    private fun saveData() {
        val data = arguments?.getString("nickname").toString()
        if (data.isNotEmpty()) {
            dataRepository.saveText(data)
            binding.saveButton.setBackgroundResource(R.drawable.va_star)
        } else {
            binding.saveButton.setBackgroundResource(R.drawable.va_nonstar)
        }
    }

    private fun userInfoText() {
        binding.userTextName.text = arguments?.getString("nickname")
        binding.userTextLevel.text = "Lv ${arguments?.getInt("level")}"

    }

    private fun userRankedText() {
        val callGetUserRanked = api.getUserRanked("${Constants.KEY}","${sharedViewModel.sharedData}")

        callGetUserRanked.enqueue(object : Callback<List<UserRanked>> {
            override fun onResponse(call: Call<List<UserRanked>>, response: Response<List<UserRanked>>) {
                val ranked = response.body()

                ranked.let {
                    setAdapter(it as ArrayList<UserRanked>)
                }
            }

            override fun onFailure(call: Call<List<UserRanked>>, t: Throwable) {

            }

        })
    }

    private fun setAdapter(rankedList: ArrayList<UserRanked>) {
        val mRankedAdapter = RankedAdapter(rankedList)
        binding.rvUserRanked.adapter = mRankedAdapter
        binding.rvUserRanked.layoutManager = LinearLayoutManager(context)
        binding.rvUserRanked.addItemDecoration(DividerItemDecoration(binding.rvUserRanked.context, LinearLayoutManager(context).orientation))
    }

    private fun fieldSquad() {
        val callGetPlayMatch = api.getPlayMatch("${Constants.KEY}","${sharedViewModel.sharedData}",52,0,1)

        callGetPlayMatch.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val jsonArray: JsonArray? = response.body()

                if (jsonArray != null && jsonArray.size() > 0) {
                    val jsonString: String = jsonArray[0].asString

                    val callGetMatchValues = api.getMatchValues("${KEY}", jsonString)

                    callGetMatchValues.enqueue(object : Callback<MatchValues> {
                        override fun onResponse(call: Call<MatchValues>, response: Response<MatchValues>) {
                            val playerInfo = response.body()

                            if (playerInfo != null) {
                                Log.e("awd",playerInfo.matchInfo.get(0).player.toString())
                            }

                        }

                        override fun onFailure(call: Call<MatchValues>, t: Throwable) {
                            Log.e("실패", "Error: ${t.message}")
                        }

                    })

                }


            }
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {

            }

        })
    }

}