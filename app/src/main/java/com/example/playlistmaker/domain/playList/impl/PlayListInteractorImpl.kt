package com.example.playlistmaker.domain.playList.impl

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.PlayListTrack
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.PlayListRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val repository: PlayListRepository): PlayListInteractor {
    override suspend fun getAll(): Flow<List<PlayList>> {
        return repository.getAll()
    }

    override suspend fun add(playlist: PlayList) {
        repository.add(playlist)
    }

    override suspend fun update(playlist: PlayList) {
        repository.update(playlist)
    }

    override suspend fun delete(playlistId: Int) {
        repository.delete(playlistId)
    }

    override suspend fun getById(id: Int): Flow<PlayList> {
        return repository.getById(id)
    }

    override suspend fun addPlaylistTracks(playlistTracks: PlayListTrack) {
        return repository.addPlaylistTracks(playlistTracks)
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        return repository.deletePlaylistTrack(playlistId, trackId)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getPlaylistTracks(playlistId)
    }
}