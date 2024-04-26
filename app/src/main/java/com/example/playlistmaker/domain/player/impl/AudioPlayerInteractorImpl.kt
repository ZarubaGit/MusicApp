package com.example.playlistmaker.domain.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.player.AudioPlayerStateObserver

class AudioPlayerInteractorImpl(
    private val mediaPlayer: MediaPlayer
) : AudioPlayerInteractor {

    var state = State.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        state = State.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        state = State.PAUSED
    }

    override fun preparePlayer(url: String?, onCompletePlaying: () -> Unit) {
        if (state == State.DEFAULT) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            setListenersPlayer({ state = State.PREPARED },
                {
                    state = State.PREPARED
                    onCompletePlaying()
                })
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        state = State.DEFAULT
    }

    override fun getCurrentState() = state

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    ) {

        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }

        mediaPlayer.setOnCompletionListener {
            onCompleteListener()
        }
    }
}