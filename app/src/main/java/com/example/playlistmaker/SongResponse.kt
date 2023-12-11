package com.example.playlistmaker

data class SongResponse(
    val resultCount: Int,
    val results: List<Track>
)