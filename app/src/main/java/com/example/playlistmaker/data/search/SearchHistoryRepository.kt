package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getSearchHistory(): List<Track>
    fun saveTrackToHistory(track: Track)
}