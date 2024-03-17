package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track

interface AudioPlayerRepository {

    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionPlayer(): Int
    fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    )
}