package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface TrackState {

    object Loading : TrackState

    object Error : TrackState

    object Empty : TrackState

    data class Content(
        val tracks: List<Track>
    ) : TrackState
}