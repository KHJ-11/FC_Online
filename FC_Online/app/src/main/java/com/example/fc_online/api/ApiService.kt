package com.example.fc_online.api

import com.example.fc_online.data.SeasonId
import com.example.fc_online.data.SpidName
import com.example.fc_online.data.TradeType
import com.example.fc_online.data.UserInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users?")
    fun getUserInfo(
        @Header("Authorization") Authorization: String,
        @Query("nickname") nickname: String
    ): Call<UserInfo>

    @GET("users/{accessid}/markets?")
    fun getTradeType(
        @Header("Authorization") Authorization: String,
        @Path("accessid") accessid: String,
        @Query("tradetype") tradetype: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<List<TradeType>>

    @GET("https://static.api.nexon.co.kr/fconline/latest/spid.json")
    fun getSpidName(
    ): Call<List<SpidName>>

    @GET("https://static.api.nexon.co.kr/fconline/latest/seasonid.json")
    fun getSeasonId(
    ): Call<List<SeasonId>>

    @GET("users/{accessid}/markets?")
    suspend fun getTradeType2(
        @Header("Authorization") Authorization: String,
        @Path("accessid") accessid: String,
        @Query("tradetype") tradetype: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<List<TradeType>>

}