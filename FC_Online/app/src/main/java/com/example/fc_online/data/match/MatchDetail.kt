package com.example.fc_online.data.match

data class MatchDetail(
    val averageRating: Float,
    val controller: String,
    val cornerKick: Int,
    val dribble: Int,
    val foul: Int,
    val injury: Int,
    val matchEndType: Int,
    val matchResult: String,
    val offsideCount: Int,
    val possession: Int,
    val redCards: Int,
    val seasonId: Int,
    val systemPause: Int,
    val yellowCards: Int
)
