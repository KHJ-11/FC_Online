package com.example.fc_online.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
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
import com.example.fc_online.data.SpidName
import com.example.fc_online.data.UserRanked
import com.example.fc_online.data.match.MatchValues
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.databinding.FragmentUserInfoTextBinding
import com.example.fc_online.databinding.ItemPlayerBinding
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

                            val play = response.body()
                            val home = play?.matchInfo?.get(0)?.player
                            Log.d("awdwad", home.toString())
                            val filter = home?.filter { it.spPosition <= 27 }

//                            val playerInfo = response.body()?.matchInfo?.get(0)?.player
//
//                            val filterlist = playerInfo?.filter { it.spPosition <= 27 }
//                            val datalist = filterlist?.map { it.spPosition }
//                            Log.e("awddawdaw123", datalist.toString())
//
//                            val layoutViews: List<ItemPlayerBinding> = listOf(
//                                binding.field.positionGK0, binding.field.positionRWB2, binding.field.positionRB3, binding.field.positionRCB4,
//                                binding.field.positionCB5, binding.field.positionLCB6, binding.field.positionLB7, binding.field.positionLWB8,
//                                binding.field.positionRDM9, binding.field.positionCDM10, binding.field.positionLDM11, binding.field.positionRM12,
//                                binding.field.positionRCM13, binding.field.positionCM14, binding.field.positionLCM15, binding.field.positionLM16,
//                                binding.field.positionRAM17, binding.field.positionCAM18, binding.field.positionLAM19, binding.field.positionRF20,
//                                binding.field.positionCF21, binding.field.positionLF22, binding.field.positionRW23, binding.field.positionRS24,
//                                binding.field.positionST25, binding.field.positionLS26, binding.field.positionLW27
//                            )


//                            if (data != null) {
//                                for (index in 0 until data.matchInfo.get(0).player.size) {
//                                    Log.e("dsadawdq", data.matchInfo.get(0).player.get(index).spPosition.toString())
//                                }
//                            }

                        }

                        override fun onFailure(call: Call<MatchValues>, t: Throwable) {
                            Log.e("실패", "Error: ${t.message}")
                        }

                    })

                }


            }
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("실패", "Error: ${t.message}")
            }

        })
    }

}