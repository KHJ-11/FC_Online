package com.example.fc_online.data

import com.google.gson.annotations.SerializedName

data class UserInfo( // 유저정보
    @SerializedName("accessId") // 유저 고유식별자
    val accessId: String = "",
    @SerializedName("nickname") // 유저 닉네임
    val nickname: String = "",
    @SerializedName("level") // 유저 레벨
    val level: Int = 0
)
