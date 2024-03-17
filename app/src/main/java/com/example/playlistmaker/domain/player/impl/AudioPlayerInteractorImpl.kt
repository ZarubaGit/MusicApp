package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.player.AudioPlayerStateObserver

class AudioPlayerInteractorImpl(
    private val medialPlayerRepository : AudioPlayerRepository
) : AudioPlayerInteractor {

    var state = State.DEFAULT

    override fun startPlayer() {
        medialPlayerRepository.startPlayer()
        state = State.PLAYING
    }

    override fun pausePlayer() {
        medialPlayerRepository.pausePlayer()
        state = State.PAUSED
    }

    override fun preparePlayer(url: String?, onCompletePlaying: () -> Unit) {
        if (state == State.DEFAULT) {
            medialPlayerRepository.preparePlayer(url)
            medialPlayerRepository.setListenersPlayer(
                { state = State.PREPARED },
                {
                    state = State.PREPARED
                    onCompletePlaying()
                })
        }
    }

    override fun releasePlayer() {
        medialPlayerRepository.releasePlayer()
        state = State.DEFAULT
    }

    override fun getCurrentState() = state

    override fun getCurrentPosition(): Int = medialPlayerRepository.getCurrentPositionPlayer()
}