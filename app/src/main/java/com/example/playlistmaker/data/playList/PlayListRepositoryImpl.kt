package com.example.playlistmaker.data.playList

import com.example.playlistmaker.data.ImageStorage
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.data.mapper.PlayListMapper
import com.example.playlistmaker.data.mapper.PlayListTrackMapper
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.PlayListTrack
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playList.PlayListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val dataBase: AppDataBase,
    private val playListMapper: PlayListMapper,
    private val storage: ImageStorage,
    private val playListTrackMapper: PlayListTrackMapper,
    private val trackMapper: TrackMapper
): PlayListRepository {
    override suspend fun getAll(): Flow<List<PlayList>> = flow{
        val playlist = dataBase.getPlayerListDao().getAll()
        playlist?.let { convertFromPlaylistEntity(it)}?.let { emit(it) }
    }

    private fun convertFromPlaylistEntity(playlist: List<PlayListEntity>) : List<PlayList> {
        return playlist.map { playListMapper.map(it) }
    }

    override suspend fun add(playlist: PlayList) {
        playlist.uri = saveImage(playlist.uri)
        dataBase.getPlayerListDao().add(playListMapper.map(playlist))
    }

    override suspend fun update(playlist: PlayList) {
        playlist.uri = saveImage(playlist.uri)
        dataBase.getPlayerListDao().update(playListMapper.map(playlist))
    }

    override suspend fun delete(playlistId: Int) {
        dataBase.getPlayerListDao().delete(playlistId)
    }
    override suspend fun getById(id: Int): Flow<PlayList> = flow {
        val item =  dataBase.getPlayerListDao().getById(id)
        emit(playListMapper.map(item))
    }

    override suspend fun addPlaylistTracks(playlistTracks: PlayListTrack) {
        dataBase.getPlayListTrackDao().addTrack(playListTrackMapper.map(playlistTracks))
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        val playlistTrackEntity: PlayListTrackEntity = dataBase.getPlayListTrackDao().get(playlistId, trackId)
        var playlistEntity: PlayListEntity = dataBase.getPlayerListDao().getById(playlistId)
        val playlist: PlayList = playListMapper.map(playlistEntity)

        if (playlist.tracks.contains(trackId)) {
            playlist.tracks.remove(trackId)
            playlist.trackTimerMillis = playlist.trackTimerMillis - playlistTrackEntity.trackTimeMillis!!
        }
        playlistEntity = playListMapper.map(playlist)

        dataBase.getPlayerListDao().update(playlistEntity)
        dataBase.getPlayListTrackDao().deleteTrack(playlistId, trackId)

        if (checkUnusedTracks(trackId)) {
            dataBase.getPlayListTrackDao().deleteTracks(trackId)
        }
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> = flow{
        val tracks = dataBase.getPlayListTrackDao().getByPlaylist(playlistId)?.map {
            trackMapper.map(it)
        }
        val favorites = dataBase.getFavoriteTrackDao().getId()
        if (favorites != null && tracks != null) {
            setFavoritesToTracks(tracks, favorites)
        }
        if (tracks != null) emit(tracks)

    }
    private fun setFavoritesToTracks(tracks: List<Track>, indicators: List<Int>) {
        for (i in tracks) {
            if (i.trackId in indicators) {
                i.isFavorite = true
            }
        }
    }

    private fun saveImage(imagePath: String?): String {
        return if(imagePath?.isEmpty() == false) {
            storage.saveImageInPrivateStorage(imagePath)
        } else {
            ""
        }
    }

    private suspend fun checkUnusedTracks(trackId: Int): Boolean {
        val playlists = dataBase.getPlayerListDao().getAll()?.let { convertFromPlaylistEntity(it) }
        return if (playlists?.isEmpty() == true) {
            true
        } else {
            val playlistsWithTrack = playlists!!.filter { playlist ->
                playlist.tracks.contains(trackId)
            }
            playlistsWithTrack.isEmpty()
        }
    }


}