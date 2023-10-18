package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class SeasonId(
    @SerializedName("seasonId")
    val seasonId: Int = 0,
    @SerializedName("className")
    val className: String = "",
    @SerializedName("seasonImg")
    val seasonImg: String = ""
)
