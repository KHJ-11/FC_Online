package com.example.fc_online.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.R
import com.example.fc_online.data.DivisionType
import com.example.fc_online.data.MatchType
import com.example.fc_online.data.UserRanked
import com.example.fc_online.util.Constants.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankedAdapter(private val rankedList: ArrayList<UserRanked>)
    : RecyclerView.Adapter<RankedAdapter.RankedHolder>() {

    inner class RankedHolder(rowRoot: View): RecyclerView.ViewHolder(rowRoot) {
        val matchItem : TextView = rowRoot.findViewById(R.id.rankedMatch)
        val divisionItem : TextView = rowRoot.findViewById(R.id.rankedDivision)
        val dateItem : TextView = rowRoot.findViewById(R.id.rankedDate)

        fun setData(item: UserRanked) {
//            matchItem.text = item.matchType.toString()
//            divisionItem.text = item.division.toString()
            dateItem.text = item.achievementDate.substring(0,10)

            val callGetMatchType = api.getMatchType()
            val callGetDivisionType = api.getDivisionType()

            callGetMatchType.enqueue(object : Callback<List<MatchType>> {
                override fun onResponse(call: Call<List<MatchType>>, response: Response<List<MatchType>>) {
                    val matchList: List<MatchType>? = response.body()
                    val filterMatchList = matchList?.filter { it.matchtype == item.matchType }
                    val filterMatchName = filterMatchList?.map { it.desc }

                    if (filterMatchList != null) {
                        matchItem.text = filterMatchName?.joinToString()
                    }
                }

                override fun onFailure(call: Call<List<MatchType>>, t: Throwable) {

                }

            })

            callGetDivisionType.enqueue(object : Callback<List<DivisionType>>{
                override fun onResponse(call: Call<List<DivisionType>>, response: Response<List<DivisionType>>) {
                    val divisionList: List<DivisionType>? = response.body()
                    val filterDivisionList = divisionList?.filter { it.divisionId == item.division }
                    val filterDivisionName = filterDivisionList?.map { it.divisionName }

                    if (filterDivisionList != null) {
                        divisionItem.text = filterDivisionName?.joinToString()
                    }
                }

                override fun onFailure(call: Call<List<DivisionType>>, t: Throwable) {

                }

            })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedAdapter.RankedHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranked, parent, false)
        return RankedHolder(view)
    }

    override fun onBindViewHolder(holder: RankedAdapter.RankedHolder, position: Int) {
        holder.setData(rankedList[position])
    }

    override fun getItemCount(): Int {
        return rankedList.size
    }
}