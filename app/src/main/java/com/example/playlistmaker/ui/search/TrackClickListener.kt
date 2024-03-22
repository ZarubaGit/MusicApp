package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

interface TrackClickListener {
    fun onTrackClicked(track: Track)
}