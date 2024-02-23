package com.example.playlistmaker.domain

class PlayPauseUseCase {

    var isPlaying: Boolean = false
    fun playingPause(): Boolean {
        isPlaying = !isPlaying
        return isPlaying

    }


}