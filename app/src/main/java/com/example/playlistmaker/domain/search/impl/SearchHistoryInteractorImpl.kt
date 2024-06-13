package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository): SearchHistoryInteractor {//внедрение зависимостей с помощью DI и Koin
    override fun addTrack(track: Track) {
        searchHistoryRepository.addTrack(track = track)
    }

    override suspend fun getFromHistory(): ArrayList<Track> {
        return searchHistoryRepository.getFromHistory()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}