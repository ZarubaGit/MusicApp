package com.example.playlistmaker.domain.favoriteTrack

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {

    suspend fun getAll(): Flow<List<Track>>

    suspend fun add(track: Track)

    suspend fun delete(track: Track)
}