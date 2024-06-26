package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.domain.models.PlayListTrack

class PlayListTrackMapper {
    fun map(playlistTracks: PlayListTrack): PlayListTrackEntity {
        return PlayListTrackEntity(
            id = playlistTracks.id,
            playlistId = playlistTracks.playlistId,
            trackId = playlistTracks.trackId,
            trackName = playlistTracks.trackName,
            artistName = playlistTracks.artistName,
            trackTimeMillis = playlistTracks.trackTimeMillis,
            artworkUrl100 = playlistTracks.artworkUrl100,
            collectionName = playlistTracks.collectionName,
            releaseDate = playlistTracks.releaseDate,
            primaryGenreName = playlistTracks.primaryGenreName,
            country = playlistTracks.country,
            previewUrl = playlistTracks.previewUrl
        )
    }
    fun map(playlistTracksEntity: PlayListTrackEntity): PlayListTrack {
        return PlayListTrack(
            id = playlistTracksEntity.id,
            playlistId = playlistTracksEntity.playlistId,
            trackId = playlistTracksEntity.trackId,
            trackName = playlistTracksEntity.trackName,
            artistName = playlistTracksEntity.artistName,
            trackTimeMillis = playlistTracksEntity.trackTimeMillis,
            artworkUrl100 = playlistTracksEntity.artworkUrl100,
            collectionName = playlistTracksEntity.collectionName,
            releaseDate = playlistTracksEntity.releaseDate,
            primaryGenreName = playlistTracksEntity.primaryGenreName,
            country = playlistTracksEntity.country,
            previewUrl = playlistTracksEntity.previewUrl
        )
    }
}