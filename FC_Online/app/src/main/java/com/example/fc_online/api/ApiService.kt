package com.example.fc_online.api

import com.example.fc_online.data.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("fifaonline4/v1.0/users?")
    fun getUserInfo(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String
    ): Call<UserInfo>

}