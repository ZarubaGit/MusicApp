package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String? = null,
    val artistName: String? = null,
    val trackTimeMillis: Int? = 0,
    val artworkUrl100: String? = null,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val previewUrl: String? = null,
    var isFavorite: Boolean = false
) : Parcelable {

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    fun getCoverArtworkForPlaylist() = artworkUrl100?.replaceAfterLast('/',"60x60bb.jpg")
}
