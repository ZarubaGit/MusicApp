package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {

    fun addTrack(track: Track)
    fun getFromHistory(): ArrayList<Track>
    fun clearHistory()
}