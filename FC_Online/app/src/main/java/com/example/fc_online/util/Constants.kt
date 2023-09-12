package com.example.fc_online.util

import com.example.fc_online.api.ApiService
import com.example.fc_online.retrofit.RetrofitClient

object Constants {

    const val BASE_URL = "https://api.nexon.co.kr/"

    const val KEY = ""

    val retrofit = RetrofitClient.getInstance()
    val api = retrofit.create(ApiService::class.java)
}