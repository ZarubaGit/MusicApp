package com.example.playlistmaker.ui.audioPlayer.view_model_audio_player



import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeStateLiveData():LiveData<PlayerState> = playerState

    private var isFavourite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavourite

    fun preparePlayer (previewUrl: String?){
        audioPlayerInteractor.preparePlayer(previewUrl) {
            playerState.postValue(PlayerState.Prepared())
            timerJob?.cancel()
        }
        playerState.postValue(PlayerState.Prepared())
    }

    private fun startPlayer(){
        audioPlayerInteractor.startPlayer()
        playerState.postValue(PlayerState.Playing(getDateFormat()))
        startTimer()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playerState.postValue(PlayerState.Paused(getDateFormat()))
        timerJob?.cancel()
    }

    private fun startTimer(){
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
                delay(DELAY_UPDATE_TIME_MS)
                playerState.postValue(PlayerState.Playing(getDateFormat()))
            }
        }

    }

    fun playbackControl(){
        if(audioPlayerInteractor.getCurrentState() == State.PLAYING) {
            pausePlayer()
        }
        else {
            startPlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasedPlayer()
    }

    private fun releasedPlayer() {
        audioPlayerInteractor.releasePlayer()
        playerState.value = PlayerState.Default()
    }

    private fun getDateFormat(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTrackInteractor.delete(track)
            } else {
                favoriteTrackInteractor.add(track)
            }
            track.isFavorite = !track.isFavorite
            isFavourite.postValue(track.isFavorite)
        }
    }

    fun checkIfFavorite(track: Track) {
        viewModelScope.launch {
            val isFavorite = favoriteTrackInteractor.isFavorite(track)
            isFavourite.postValue(isFavorite)
        }
    }

    companion object{
        private const val DELAY_UPDATE_TIME_MS = 300L

    }

}
