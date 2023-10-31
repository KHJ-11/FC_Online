package com.example.fc_online.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.R
import com.example.fc_online.util.DataRepository

class SaveNameAdapter(private val data: List<String>)
    : RecyclerView.Adapter<SaveNameAdapter.Nameholder>() {

    private lateinit var dataRepository: DataRepository

    inner class Nameholder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {
        val nameItem: TextView = rowRoot.findViewById(R.id.textSaveName)
        val closeItem: ImageView = rowRoot.findViewById(R.id.iv_close)

//        fun setData(item: SaveName) {
//            nameItem.text = dataRepository.getData()
//        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveNameAdapter.Nameholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_save_nickname, parent, false)
        return Nameholder(view)
    }

    override fun onBindViewHolder(holder: SaveNameAdapter.Nameholder, position: Int) {
        val text = data[position]
//        holder.setData(nameList[position])
        holder.nameItem.text = data[position]


    }

    override fun getItemCount(): Int {
        return data.size
    }
}