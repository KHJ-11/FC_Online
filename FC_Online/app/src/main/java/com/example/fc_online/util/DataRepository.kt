package com.example.fc_online.util

import android.content.Context

class DataRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveData(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("key", data)
        editor.apply()
    }

    fun getData(): String {
        return sharedPreferences.getString("key", "") ?: ""
    }
}