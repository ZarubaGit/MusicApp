package com.example.playlistmaker.domain.favoriteTrack.impl

import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(private val favoriteTrackRepository: FavoriteTrackRepository
): FavoriteTrackInteractor {
    override suspend fun getAll(): Flow<List<Track>> {
        return favoriteTrackRepository.getAll()
    }

    override suspend fun add(track: Track) {
        favoriteTrackRepository.add(track)
    }

    override suspend fun delete(track: Track) {
        favoriteTrackRepository.delete(track)
    }
}