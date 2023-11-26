package com.example.fc_online.ui.adapter

import android.graphics.LinearGradient
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.R
import com.example.fc_online.data.PlayMatch
import com.example.fc_online.data.match.MatchValues
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class PlayAdapter(private val playList: ArrayList<PlayMatch>)
    : RecyclerView.Adapter<PlayAdapter.PlayHolder>() {
   inner class PlayHolder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {
       val matchDate: TextView = rowRoot.findViewById(R.id.matchDate)
       val homeUser: TextView = rowRoot.findViewById(R.id.matchHome)
       val homeScore: TextView = rowRoot.findViewById(R.id.matchHomeScore)
       val awayUser: TextView = rowRoot.findViewById(R.id.matchAway)
       val awayScore: TextView = rowRoot.findViewById(R.id.matchAwayScore)
       val itemPlayRecord: LinearLayout = rowRoot.findViewById(R.id.item_play_record)

       fun setData(item: PlayMatch) {
           val matchIds = item.playMatch.replace("[^A-Za-z0-9]".toRegex(), "")

           val callGetMatchValues = api.getMatchValues("${KEY}","${matchIds}")

           callGetMatchValues.enqueue(object : Callback<MatchValues> {
               override fun onResponse(call: Call<MatchValues>, response: Response<MatchValues>) {
                   val match = response.body()
                   match.let {
                       setValues(response.body()!!)
                   }
               }

               override fun onFailure(call: Call<MatchValues>, t: Throwable) {
                   Log.e("실패", "Error: ${t.message}")
               }

           })
       }

       fun setValues(value: MatchValues) {
           homeUser.text = value.matchInfo.get(0).nickname
           awayUser.text = value.matchInfo.get(1).nickname
           homeScore.text = value.matchInfo.get(0).shoot.goalTotalDisplay.toString()
           awayScore.text = value.matchInfo.get(1).shoot.goalTotalDisplay.toString()

           val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
           val outputFormat = SimpleDateFormat("yyyy.MM.dd. a hh:mm:ss", Locale.getDefault())
           try {
               val date = inputFormat.parse(value.matchDate)
               val outputDateString = outputFormat.format(date)
               matchDate.text = outputDateString
           } catch (e: Exception) {
               e.printStackTrace()
           }

           when(value.matchInfo.get(0).matchDetail.matchResult) {
               "무" -> itemPlayRecord.setBackgroundResource(R.drawable.rounded_draw)
               "승" -> itemPlayRecord.setBackgroundResource(R.drawable.rounded_win)
               "패" -> itemPlayRecord.setBackgroundResource(R.drawable.rounded_lose)
           }

           val test = value.matchInfo.get(0).matchDetail.matchResult

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_play_record, parent, false)
        return PlayHolder(view)

    }

    override fun onBindViewHolder(holder: PlayHolder, position: Int) {
        holder.setData(playList[position])

    }

    override fun getItemCount(): Int {
        return playList.size

    }
}