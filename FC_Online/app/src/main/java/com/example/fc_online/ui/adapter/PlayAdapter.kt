package com.example.fc_online.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.R
import com.example.fc_online.data.PlayMatch

class PlayAdapter(private val playList: ArrayList<PlayMatch>)
    : RecyclerView.Adapter<PlayAdapter.PlayHolder>() {
   inner class PlayHolder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {
        val matchid: TextView = rowRoot.findViewById(R.id.matchId)

       fun setData(item: PlayMatch) {
           val matchIds = item.playMatch.replace("[^A-Za-z0-9]".toRegex(), "")
           matchid.text = matchIds
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_play, parent, false)
        return PlayHolder(view)

    }

    override fun onBindViewHolder(holder: PlayHolder, position: Int) {
        holder.setData(playList[position])

    }

    override fun getItemCount(): Int {
        return playList.size

    }
}