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

                                                    // 추후 로직 간략화 예정
                                                    filterPositionList?.get(index)?.takeIf { it == 0 }?.let {
                                                        binding.field.position0.apply {
                                                            itemPlayerUi.visibility = View.VISIBLE
                                                            fieldPosition.text = "GK"
                                                            fieldName.text = filterNameList?.joinToString()
                                                            fieldGrade.text = home.get(index).spGrade.toString()
                                                            if (grade == 1) {
                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
                                                            } else if (grade <= 4) {
                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
                                                            } else if (grade <= 7) {
                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
                                                            } else if (grade <= 10) {
                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
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
//                                                    filterPositionList?.get(index)?.takeIf { it == 2 }?.let {
//                                                        binding.field.position2.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RWB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 3 }?.let {
//                                                        binding.field.position3.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 4 }?.let {
//                                                        binding.field.position4.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RCB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 5 }?.let {
//                                                        binding.field.position5.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "CB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 6 }?.let {
//                                                        binding.field.position6.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LCB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 7 }?.let {
//                                                        binding.field.position7.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 8 }?.let {
//                                                        binding.field.position8.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LWB"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 9 }?.let {
//                                                        binding.field.position9.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RDM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 10 }?.let {
//                                                        binding.field.position10.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "CDM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 11 }?.let {
//                                                        binding.field.position11.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LDM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 12 }?.let {
//                                                        binding.field.position12.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 13 }?.let {
//                                                        binding.field.position13.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RCM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 14 }?.let {
//                                                        binding.field.position14.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "CM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 15 }?.let {
//                                                        binding.field.position15.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LCM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 16 }?.let {
//                                                        binding.field.position16.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 17 }?.let {
//                                                        binding.field.position17.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RAM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 18 }?.let {
//                                                        binding.field.position18.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "CAM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 19 }?.let {
//                                                        binding.field.position19.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LAM"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 20 }?.let {
//                                                        binding.field.position20.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RF"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 21 }?.let {
//                                                        binding.field.position21.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "CF"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 22 }?.let {
//                                                        binding.field.position22.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LF"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 23 }?.let {
//                                                        binding.field.position23.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RW"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 24 }?.let {
//                                                        binding.field.position24.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "RS"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 25 }?.let {
//                                                        binding.field.position25.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "ST"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 26 }?.let {
//                                                        binding.field.position26.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LS"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }
//                                                    filterPositionList?.get(index)?.takeIf { it == 27 }?.let {
//                                                        binding.field.position27.apply {
//                                                            itemPlayerUi.visibility = View.VISIBLE
//                                                            fieldPosition.text = "LW"
//                                                            fieldName.text = filterNameList?.joinToString()
//                                                            fieldGrade.text = home.get(index).spGrade.toString()
//                                                            if (grade == 1) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_black)
//                                                                fieldGrade.setTextColor(Color.parseColor("#C3C7C8"))
//                                                            } else if (grade <= 4) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_bronze)
//                                                                fieldGrade.setTextColor(Color.parseColor("#7F3F25"))
//                                                            } else if (grade <= 7) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_silver)
//                                                                fieldGrade.setTextColor(Color.parseColor("#5C6169"))
//                                                            } else if (grade <= 10) {
//                                                                fieldGrade.setBackgroundResource(R.drawable.back_gold)
//                                                                fieldGrade.setTextColor(Color.parseColor("#6C5200"))
//                                                            }
//                                                            Glide.with(this@UserInfoText)
//                                                                .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
//                                                                .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
//                                                                .into(fieldPicture)
//                                                            Glide.with(this@UserInfoText)
//                                                                .load(season)
//                                                                .into(fieldSeason)
//                                                        }
//                                                    }


                                                    fun setPlayerField(position: Int, positionText: String) {
                                                        binding.field.apply {
                                                            val playerUi = when(position) {
                                                                0 -> position0
                                                                2 -> position2
                                                                3 -> position3
                                                                else -> return
                                                            }
                                                            playerUi.apply {
                                                                itemPlayerUi.visibility = View.VISIBLE
                                                                fieldPosition.text = positionText
                                                                fieldName.text = filterNameList?.joinToString()
                                                                fieldGrade.text = home.get(index).spGrade.toString()
                                                            }
                                                        }
                                                    }
                                                    filterPositionList?.get(index)?.takeIf { it in 0..2 }?.let {
                                                        val positionText = when (it) {
                                                            0 -> "GK"
                                                            1 -> "CB"
                                                            2 -> "RWB"
                                                            // 여러 위치에 대한 케이스 추가 가능
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