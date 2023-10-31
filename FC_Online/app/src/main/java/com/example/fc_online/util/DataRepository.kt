package com.example.fc_online.util

import android.content.Context

class DataRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveText(text: String) {
        val currentTexts = getTexts().toMutableSet()
        currentTexts.add(text)
        editor.putStringSet("texts", currentTexts)
        editor.apply()
//        sharedPreferences.edit().putStringSet("texts", currentTexts).apply()
    }

    fun getTexts(): Set<String> {
        return sharedPreferences.getStringSet("texts", emptySet()) ?: emptySet()
    }

//    fun removeText(text: String) {
//        val currentTexts = getTexts().toMutableSet()
//        currentTexts.remove(text)
////        editor.putStringSet("texts", currentTexts)
////        editor.apply()
//        sharedPreferences.edit().putStringSet("texts", currentTexts).apply()
//    }

}