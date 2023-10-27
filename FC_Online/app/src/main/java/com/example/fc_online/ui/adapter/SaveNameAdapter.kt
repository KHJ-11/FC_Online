package com.example.fc_online.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.data.SaveName

class SaveNameAdapter(private val nameList: ArrayList<SaveName>)
    : RecyclerView.Adapter<SaveNameAdapter.Nameholder>() {

    inner class Nameholder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveNameAdapter.Nameholder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SaveNameAdapter.Nameholder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}