package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.PlayListTrackEntity

@Dao
interface PlayListTrackDao {
    @Insert(entity = PlayListTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(playListTrack: PlayListTrackEntity)

    @Query("SELECT * FROM playlist_tracks_entity WHERE playlistId=:id ORDER BY id DESC")
    suspend fun getByPlaylist(id: Int): List<PlayListTrackEntity>?

    @Query("DELETE FROM playlist_tracks_entity WHERE playlistId=:playlistId AND trackId=:trackId")
    suspend fun deleteTrack(playlistId: Int, trackId: Int)

    @Query("DELETE FROM playlist_tracks_entity WHERE trackId=:trackId")
    suspend fun deleteTracks(trackId: Int)

    @Query("SELECT * FROM playlist_tracks_entity WHERE playlistId=:playlistId AND trackId=:trackId")
    suspend fun get(playlistId: Int, trackId: Int): PlayListTrackEntity
}