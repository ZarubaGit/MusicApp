package com.example.playlistmaker.data.playList

import com.example.playlistmaker.data.ImageStorage
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.mapper.PlayListMapper
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playList.PlayListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val dataBase: AppDataBase,
    private val playListMapper: PlayListMapper,
    private val storage: ImageStorage
): PlayListRepository {
    override suspend fun getAll(): Flow<List<PlayList>> = flow{
        val playlist = dataBase.getPlayerListDao().getAll()
        playlist?.let { convertFromPlaylistEntity(it)}?.let { emit(it) }
    }

    private fun convertFromPlaylistEntity(playlist: List<PlayListEntity>) : List<PlayList> {
        return playlist.map { playListMapper.map(it) }
    }

    override suspend fun add(playlist: PlayList) {
        if (playlist.uri != null && playlist.uri !="") {
            playlist.uri = storage.saveImageInPrivateStorage(playlist.uri!!)
        }
        dataBase.getPlayerListDao().add(playListMapper.map(playlist))
    }

    override suspend fun update(playlist: PlayList) {
        dataBase.getPlayerListDao().update(playListMapper.map(playlist))
    }
}