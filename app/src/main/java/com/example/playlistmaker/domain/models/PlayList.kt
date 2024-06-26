package com.example.playlistmaker.domain.models

data class PlayList(
    val id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var uri: String? = null,
    val tracks: ArrayList<Int>,
    var trackTimerMillis: Int = 0
)
