package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.PlayListTrack
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun getAll(): Flow<List<PlayList>>
    suspend fun add(playlist: PlayList)
    suspend fun update(playlist: PlayList)
    suspend fun delete(playlistId: Int)
    suspend fun getById(id: Int): Flow<PlayList>
    suspend fun addPlaylistTracks(playlistTracks: PlayListTrack)
    suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int)
    suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>>
}