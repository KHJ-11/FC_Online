package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class PlayMatch( // 경기 기록
    @SerializedName("playMatch")
    val playMatch: String = "" // 유저가 플레이한 매치의 고유 식별자 목록
)
