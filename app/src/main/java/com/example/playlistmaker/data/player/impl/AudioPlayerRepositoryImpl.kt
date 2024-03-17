package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.player.AudioPlayerStateObserver
import com.example.playlistmaker.domain.player.model.AudioPlayerState
import java.io.IOException

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): AudioPlayerRepository {


    override fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPositionPlayer(): Int = mediaPlayer.currentPosition

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