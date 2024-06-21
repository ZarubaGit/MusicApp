package com.example.playlistmaker.ui.media.playList

import com.example.playlistmaker.domain.models.PlayList

sealed interface PlayListState {
    data class Content(val playlists: List<PlayList>) : PlayListState
    object Empty: PlayListState
}