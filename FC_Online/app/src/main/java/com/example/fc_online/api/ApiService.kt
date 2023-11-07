package com.example.fc_online.api

import com.example.fc_online.data.DivisionType
import com.example.fc_online.data.MatchType
import com.example.fc_online.data.MatchValues
import com.example.fc_online.data.SeasonId
import com.example.fc_online.data.SpidName
import com.example.fc_online.data.TradeType
import com.example.fc_online.data.UserInfo
import com.example.fc_online.data.UserRanked
import com.google.gson.JsonArray
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

//    @GET("users/{accessid}/markets?")
//    suspend fun getTradeType2(
//        @Header("Authorization") Authorization: String,
//        @Path("accessid") accessid: String,
//        @Query("tradetype") tradetype: String,
//        @Query("offset") offset: Int,
//        @Query("limit") limit: Int
//    ): Response<List<TradeType>>

    @GET("users/{accessid}/maxdivision")
    fun getUserRanked(
        @Header("Authorization") Authorization: String,
        @Path("accessid") accessid: String
    ): Call<List<UserRanked>>

    @GET("https://static.api.nexon.co.kr/fconline/latest/matchtype.json")
    fun getMatchType(
    ): Call<List<MatchType>>

    @GET("https://static.api.nexon.co.kr/fconline/latest/division.json")
    fun getDivisionType(
    ): Call<List<DivisionType>>

    @GET("users/{accessid}/matches?")
    fun getPlayMatch(
        @Header("Authorization") Authorization: String,
        @Path("accessid") accessid: String,
        @Query("matchtype") matchtype: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ) : Call<JsonArray>

    @GET("matches/{matchid}")
    fun getMatchValues(
        @Header("Authorization") Authorization: String,
        @Path("matchid") matchId: String
    ) : Call<MatchValues>

}