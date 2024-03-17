package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.AudioPlayerState

interface AudioPlayerStateObserver {
    fun onAudioPlayerStateChanged(state: AudioPlayerState)
}