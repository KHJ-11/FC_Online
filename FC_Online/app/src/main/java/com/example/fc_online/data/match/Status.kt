package com.example.fc_online.data.match

data class Status(
    val aerialSuccess: Int,
    val aerialTry: Int,
    val assist: Int,
    val ballPossesionSuccess: Int,
    val ballPossesionTry: Int,
    val block: Int,
    val blockTry: Int,
    val defending: Int,
    val dribble: Int,
    val dribbleSuccess: Int,
    val dribbleTry: Int,
    val effectiveShoot: Int,
    val goal: Int,
    val intercept: Int,
    val passSuccess: Int,
    val passTry: Int,
    val redCards: Int,
    val shoot: Int,
    val spRating: Double,
    val tackle: Int,
    val tackleTry: Int,
    val yellowCards: Int
)
