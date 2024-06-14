package com.example.playlistmaker.ui.media.favoriteSongFragmentViewModel

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteState {
data class Content(val track: List<Track>): FavoriteState

object Empty: FavoriteState
}