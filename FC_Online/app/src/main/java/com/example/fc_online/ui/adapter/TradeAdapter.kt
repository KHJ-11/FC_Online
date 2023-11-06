package com.example.fc_online.ui.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fc_online.R
import com.example.fc_online.data.SeasonId
import com.example.fc_online.data.SpidName
import com.example.fc_online.data.TradeType
import com.example.fc_online.util.Constants.api
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.text.DecimalFormat
import kotlin.math.log
import kotlin.time.Duration.Companion.nanoseconds

class TradeAdapter(private val tradeList:ArrayList<TradeType>)
    : RecyclerView.Adapter<TradeAdapter.Tradeholder>() {

    inner class Tradeholder(rowRoot: View) : RecyclerView.ViewHolder(rowRoot) {
        val spidItem: TextView = rowRoot.findViewById(R.id.tradeSpid)
        val gradeItem: TextView = rowRoot.findViewById(R.id.tradeGrade)
        val valueItem: TextView = rowRoot.findViewById(R.id.tradeValue)
        val spidPicture: ImageView = rowRoot.findViewById(R.id.tradePicture)
        val spidSeason: ImageView = rowRoot.findViewById(R.id.tradeSeason)

        fun setData(item: TradeType) {
            gradeItem.text = item.grade.toString()

            val number = item.value.toString().toLong()
            val unitArray = arrayOf("","만","억","조")

            val units = mutableListOf<String>()
            var remainingNumber = number

            for (unit in unitArray) {
                val unitValue = remainingNumber % 10000
                if (unitValue > 0) {
                    units.add(unitValue.toString() + unit)
                }
                remainingNumber /= 10000
            }
            val koreanRepresentation = units.reversed().joinToString(" ")

            valueItem.text = koreanRepresentation


            if (item.grade == 1) {
                gradeItem.setBackgroundResource(R.drawable.back_black)
                gradeItem.setTextColor(Color.parseColor("#C3C7C8"))
            } else if (item.grade <= 4) {
                gradeItem.setBackgroundResource(R.drawable.back_bronze)
                gradeItem.setTextColor(Color.parseColor("#7F3F25"))
            } else if (item.grade <= 7) {
                gradeItem.setBackgroundResource(R.drawable.back_silver)
                gradeItem.setTextColor(Color.parseColor("#5C6169"))
            } else if (item.grade <= 10) {
                gradeItem.setBackgroundResource(R.drawable.back_gold)
                gradeItem.setTextColor(Color.parseColor("#6C5200"))
            }

            val callGetSpidName = api.getSpidName()

            callGetSpidName.enqueue(object : Callback<List<SpidName>> {
                override fun onResponse(call: Call<List<SpidName>>, response: Response<List<SpidName>>) {
                    val dataList: List<SpidName>? = response.body()

                    val filterDataList = dataList?.filter { it.id == item.spid }
                    val filterNameList = filterDataList?.map { it.name }

                    val spid = item.spid.toString()
                    val pid = spid.substring(spid.length - 6, spid.length)
                    val id = pid.replace("^0+".toRegex(),"")
                    val sid = spid.substring(0 until 3)

                    if (filterDataList != null) {
                        spidItem.text = filterNameList?.joinToString()
                        Glide.with(itemView.context)
                            .load("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p${spid}.png")
                            .error("https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${id}.png")
                            .into(spidPicture)
                    }

                    val callGetSeasonId = api.getSeasonId()
                    callGetSeasonId.enqueue(object : Callback<List<SeasonId>> {
                        override fun onResponse(call: Call<List<SeasonId>>, response: Response<List<SeasonId>>) {
                            val seasonList: List<SeasonId>? = response.body()

                            val filterSeasonList = seasonList?.filter { it.seasonId == sid.toInt() }
                            val filterSimageList = filterSeasonList?.map { it.seasonImg }

                            val season = filterSimageList?.joinToString()

                            Glide.with(itemView.context)
                                .load(season)
                                .into(spidSeason)

                        }

                        override fun onFailure(call: Call<List<SeasonId>>, t: Throwable) {

                        }

                    })

                }

                override fun onFailure(call: Call<List<SpidName>>, t: Throwable) {

                }
            })

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeAdapter.Tradeholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trade, parent, false)
        return Tradeholder(view)
    }

    override fun onBindViewHolder(holder: TradeAdapter.Tradeholder, position: Int) {
        holder.setData(tradeList[position])
    }

    override fun getItemCount(): Int {
        return tradeList.size
    }

}