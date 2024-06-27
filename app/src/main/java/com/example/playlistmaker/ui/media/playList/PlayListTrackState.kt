package com.example.playlistmaker.ui.media.playList

import com.example.playlistmaker.domain.models.Track

sealed interface PlayListTrackState {
    class Match(val playlistName: String? = null): PlayListTrackState
    class Added(val playlistName: String? =  null): PlayListTrackState
    class Content(val tracks: List<Track>): PlayListTrackState
    class Removed(val playlistName: String? = null): PlayListTrackState
    object Empty: PlayListTrackState
}