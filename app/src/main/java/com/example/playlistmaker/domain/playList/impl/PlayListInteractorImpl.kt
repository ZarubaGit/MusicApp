package com.example.playlistmaker.domain.playList.impl

import com.example.playlistmaker.domain.models.PlayList
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
}