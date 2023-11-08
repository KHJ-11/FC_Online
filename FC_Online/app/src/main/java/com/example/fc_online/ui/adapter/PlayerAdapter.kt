package com.example.fc_online.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.data.match.Player

class PlayerAdapter(private val playerList: ArrayList<Player>)
    : RecyclerView.Adapter<PlayerAdapter.PlayerHolder>() {
    inner class PlayerHolder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerAdapter.PlayerHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PlayerAdapter.PlayerHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}