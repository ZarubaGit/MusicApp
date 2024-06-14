package com.example.playlistmaker.data.favoriteTrack

import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.data.mapper.FavoriteTrackMapper
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(private val appDataBase: AppDataBase,
    private val favoriteTrackMapper: FavoriteTrackMapper
) : FavoriteTrackRepository{
    override suspend fun getAll(): Flow<List<Track>> = flow {
        val track = appDataBase.getFavoriteTrackDao().getAll()
        track?.let { convertFromFavoriteTrackEntity(it) }?.let { emit(it) }
    }

    private fun convertFromFavoriteTrackEntity(track: List<FavoriteTrackEntity>): List<Track> {
        return track.map { favoriteTrackMapper.map(it) }
    }

    override suspend fun add(track: Track) {
        appDataBase.getFavoriteTrackDao().add(favoriteTrackMapper.map(track))
    }

    override suspend fun delete(track: Track) {
        appDataBase.getFavoriteTrackDao().delete(favoriteTrackMapper.map(track))
    }

    override suspend fun isFavorite(track: Track): Boolean {
        return appDataBase.getFavoriteTrackDao().isFavorite(track.trackId) != null
    }

}