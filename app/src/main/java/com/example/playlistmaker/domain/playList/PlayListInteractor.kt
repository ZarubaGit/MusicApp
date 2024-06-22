package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.models.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    suspend fun getAll(): Flow<List<PlayList>>
    suspend fun add(playlist: PlayList)
    suspend fun update(playlist: PlayList)
}