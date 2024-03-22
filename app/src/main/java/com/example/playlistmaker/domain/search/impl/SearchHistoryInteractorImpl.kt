package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository): SearchHistoryInteractor {
    override fun addTrack(track: Track) {
        searchHistoryRepository.addTrack(track = track)
    }

    override fun getFromHistory(): ArrayList<Track> {
        return searchHistoryRepository.getFromHistory()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}