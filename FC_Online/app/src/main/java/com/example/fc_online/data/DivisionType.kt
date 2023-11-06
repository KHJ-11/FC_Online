package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class DivisionType( // 등급정보
    @SerializedName("divisionId") // 등급 식별자
    val divisionId: Int = 0,
    @SerializedName("divisionName") // 등급 명
    val divisionName: String = ""
)
