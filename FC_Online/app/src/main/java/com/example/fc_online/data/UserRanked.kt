package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class UserRanked( // 최고등급
    @SerializedName("matchType") // 매치 종류
    val matchType: Int = 0,
    @SerializedName("division") // 등급 식별자
    val division: Int = 0,
    @SerializedName("achievementDate") // 최고등급 달성일자
    val achievementDate: String = ""
)