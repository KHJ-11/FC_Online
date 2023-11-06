package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class MatchType( // 매치정보
    @SerializedName("matchtype") // 매치 종류
    val matchtype: Int = 0,
    @SerializedName("desc") // 매치 명
    val desc: String = ""
)
