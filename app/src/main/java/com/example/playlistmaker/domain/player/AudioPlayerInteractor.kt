package com.example.playlistmaker.domain.player


import com.example.playlistmaker.domain.models.State


interface AudioPlayerInteractor {
    fun preparePlayer(url:String?, onCompletePlaying:() -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentState(): State
    fun getCurrentPosition(): Int

    fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    )

}