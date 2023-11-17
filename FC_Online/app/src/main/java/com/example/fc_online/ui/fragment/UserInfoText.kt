package com.example.fc_online.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import com.bumptech.glide.Glide
import com.example.fc_online.MainActivity
import com.example.fc_online.R
import com.example.fc_online.data.PlayMatch
import com.example.fc_online.data.SaveName
import com.example.fc_online.data.SeasonId
import com.example.fc_online.data.SpidName
import com.example.fc_online.data.UserRanked
import com.example.fc_online.data.match.MatchValues
import com.example.fc_online.data.match.Player
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

//                            val filterPosition = home?.filter { it.spPosition <= 27 }
//                            val positionList = filterPosition?.map { it.spPosition }
                            val filterPositionList = home?.map { it.spPosition }
//                            val positionSort = positionList?.sorted()

                            if (filterPositionList != null) {
                                for (index in 0 until filterPositionList.size) {

                                    val spid = home.get(index).spId.toString()
                                    val pid = spid.substring(spid.length - 6, spid.length)
                                    val id = pid.replace("^0+".toRegex(),"")
                                    val sid = spid.substring(0 until 3)

                                    val grade = home.get(index).spGrade

                                    val callGetSpidName = api.getSpidName()

                                    callGetSpidName.enqueue(object : Callback<List<SpidName>> {
                                        override fun onResponse(call: Call<List<SpidName>>, response: Response<List<SpidName>>) {
                                            val nameList: List<SpidName>? = response.body()

                                            val filterName = nameList?.filter { it.id == home.get(index).spId }
                                            val filterNameList = filterName?.map { it.name }

                                            val callGetSeasonId = api.getSeasonId()

                                            callGetSeasonId.enqueue(object : Callback<List<SeasonId>> {
                                                override fun onResponse(call: Call<List<SeasonId>>, response: Response<List<SeasonId>>) {
                                                    val seasonList: List<SeasonId>? = response.body()

                                                    val filterSeason = seasonList?.filter { it.seasonId == sid.toInt() }
                                                    val filterSeasonImage = filterSeason?.map { it.seasonImg }
                                                    val season = filterSeasonImage?.joinToString()


                                                    fun setPlayerField(position: Int, positionText: String) {
                                                        binding.field.apply {
                                                            val playerUi = when(position) {
                                                                0 -> position0
                                                                2 -> position2
                                                                3 -> position3
                                                                4 -> position4
                                                                5 -> position5
                                                                6 -> position6
                                                                7 -> position7
                                                                8 -> position8
                                                                9 -> position9
                                                                10 -> position10
                                                                11 -> position11
                                                                12 -> position12
                                                                13 -> position13
                                                                14 -> position14
                                                                15 -> position15
                                                                16 -> position16
                                                                17 -> position17
                                                                18 -> position18
                                                                19 -> position19
                                                                20 -> position20
                                                                21 -> position21
                                                                22 -> position22
                                                                23 -> position23
                                                                24 -> position24
                                                                25 -> position25
                                                                26 -> position26
                                                                27 -> position27
                                                                else -> return
                                                            }

                                                            playerUi.apply {
                                                                itemPlayerUi.visibility = View.VISIBLE
                                                                fieldPosition.text = positionText
                                                                fieldName.text = filterNameList?.joinToString()
                                                                fieldGrade.text = home.get(index).spGrade.toString()

                                                                when(grade) {
                                                                    1 -> {
                                                                        fieldGrade.setBackgroundResource(R.drawable.back_black)
                                                                        fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
                                                                    }
                                                                    in 2..4 -> {
                                                                        fieldGrade.setBackgroundResource(R.drawable.back_bronze)
                                                                        fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
                                                                    }
                                                                    in 5..7 -> {
                                                                        fieldGrade.setBackgroundResource(R.drawable.back_silver)
                                                                        fieldGrade.setTextColor(Color.parseColor("#5C6169"))
                                                                    }
                                                                    in 8..10 -> {
                                                                        fieldGrade.setBackgroundResource(R.drawable.back_gold)
                                                                        fieldGrade.setTextColor(Color.parseColor("#6C5200"))
                                                                    }
                                                                }

                                                                Glide.with(this@UserInfoText)
                                                                    .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
                                                                    .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
                                                                    .into(fieldPicture)

                                                                Glide.with(this@UserInfoText)
                                                                    .load(season)
                                                                    .into(fieldSeason)

                                                            }
                                                        }
                                                    }

                                                    filterPositionList?.get(index)?.takeIf { it in 0..27 }?.let {
                                                        val positionText = when(it) {
                                                            0 -> "GK"
                                                            2 -> "RWB"
                                                            3 -> "RB"
                                                            4 -> "RCB"
                                                            5 -> "CB"
                                                            6 -> "LCB"
                                                            7 -> "LB"
                                                            8 -> "LWB"
                                                            9 -> "RDM"
                                                            10 -> "CDM"
                                                            11-> "LDM"
                                                            12 -> "RM"
                                                            13 -> "RCM"
                                                            14 -> "CM"
                                                            15 -> "LCM"
                                                            16 -> "LM"
                                                            17 -> "RAM"
                                                            18 -> "CAM"
                                                            19 -> "LAM"
                                                            20 -> "RF"
                                                            21 -> "CF"
                                                            22 -> "LF"
                                                            23 -> "RW"
                                                            24 -> "RS"
                                                            25 -> "ST"
                                                            26 -> "LS"
                                                            27 -> "LW"
                                                            else -> ""
                                                        }

                                                        setPlayerField(it, positionText)
                                                    }


                                                }

                                                override fun onFailure(call: Call<List<SeasonId>>, t: Throwable) {
                                                    Log.e("실패", "Error: ${t.message}")
                                                }

                                            })
                                        }

                                        override fun onFailure(call: Call<List<SpidName>>, t: Throwable) {
                                            Log.e("실패", "Error: ${t.message}")
                                        }

                                    })

                                }
                            }


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