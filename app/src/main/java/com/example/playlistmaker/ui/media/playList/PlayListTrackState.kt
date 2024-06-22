package com.example.playlistmaker.ui.media.playList

sealed interface PlayListTrackState {
    class Match(val playlistName: String? = null): PlayListTrackState
    class Added(val playlistName: String? =  null): PlayListTrackState
}