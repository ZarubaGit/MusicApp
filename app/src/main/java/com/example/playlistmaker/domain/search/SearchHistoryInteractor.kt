package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    suspend fun getFromHistory(): ArrayList<Track>
    fun clearHistory()

}