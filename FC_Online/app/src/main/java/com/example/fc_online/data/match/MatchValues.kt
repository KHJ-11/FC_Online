package com.example.fc_online.data.match

data class MatchValues(
    val matchDate: String,
    val matchId: String,
    val matchInfo: List<MatchInfo>,
    val matchType: Int
)
