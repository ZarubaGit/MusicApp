package com.example.playlistmaker.data.dto

import java.io.Serializable

data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int? = 0,
    val artworkUrl100: String,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val previewUrl: String? = null
) : Serializable


