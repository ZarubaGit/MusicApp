package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

object TrackMapper {
    fun map(track: Track): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = convertToYear(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
    private fun convertToYear(releaseDate: String?): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date.from(Instant.parse(releaseDate)))
    }
}