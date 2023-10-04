package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class TradeType( // 거래기록
    @SerializedName("tradeDate") // 거래일자(ex. 2019-05-13T18:03:10)
    val tradeDate: String = "",
    @SerializedName("saleSn") // 거래 고유식별자
    val saleSn: String = "",
    @SerializedName("spid") // 선수 고유식별자
    val spid: Int = 0,
    @SerializedName("grade") // 선수 강화등급
    val grade: Int = 0,
    @SerializedName("value") // 선수 가치(BP)
    val value: Int = 0
)
