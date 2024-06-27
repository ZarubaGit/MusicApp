package com.example.playlistmaker.ui.audioPlayer.view_model_audio_player


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.PlayListTrack
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.ui.audioPlayer.PlayerState
import com.example.playlistmaker.ui.media.playList.PlayListState
import com.example.playlistmaker.ui.media.playList.PlayListTrackState
import com.example.playlistmaker.util.Changer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeStateLiveData(): LiveData<PlayerState> = playerState

    private var isFavourite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavourite

    private val playlistState = MutableLiveData<PlayListState>(PlayListState.Empty)
    fun observePlaylistState(): LiveData<PlayListState> = playlistState

    private val addedToPlaylistState = MutableLiveData<PlayListTrackState>()
    fun observeAddedToPlaylistState(): LiveData<PlayListTrackState> = addedToPlaylistState

    fun preparePlayer(previewUrl: String?) {
        audioPlayerInteractor.preparePlayer(previewUrl) {
            playerState.postValue(PlayerState.Prepared())
            timerJob?.cancel()
        }
        playerState.postValue(PlayerState.Prepared())
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playerState.postValue(PlayerState.Playing(getDateFormat()))
        startTimer()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playerState.postValue(PlayerState.Paused(getDateFormat()))
        timerJob?.cancel()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
                delay(DELAY_UPDATE_TIME_MS)
                playerState.postValue(PlayerState.Playing(getDateFormat()))
            }
        }

    }

    fun playbackControl() {
        if (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
            pausePlayer()
        } else {
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
        return Changer.convertMillisToMinutesAndSeconds(audioPlayerInteractor.getCurrentPosition())
            ?: "00:00"
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

    fun addTrackInPlaylist(playlist: PlayList, track: Track) {
        if (playlist.tracks.isEmpty()) {
            addTrackToPlaylist(playlist, track)
            addedToPlaylistState.postValue(PlayListTrackState.Added(playlist.name))
        } else {
            if (playlist.tracks.contains(track.trackId)) {
                addedToPlaylistState.postValue(PlayListTrackState.Match(playlist.name))
            } else {
                addTrackToPlaylist(playlist, track)
                addedToPlaylistState.postValue(PlayListTrackState.Added(playlist.name))
            }
        }
    }




    private fun addTrackToPlaylist(playlist: PlayList, track: Track) {
        playlist.tracks.add(track.trackId)
        playlist.trackTimerMillis += track.trackTimeMillis ?: 0

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playListInteractor.update(playlist)
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val playlistTrack = PlayListTrack(
                    id = null,
                    playlistId = playlist.id!!,
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl
                )
                playListInteractor.addPlaylistTracks(playlistTrack)
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playListInteractor.getAll().collect {
                if (it.isEmpty()) {
                    renderState(PlayListState.Empty)
                } else {
                    renderState(PlayListState.Content(it))
                }
            }
        }
    }

    private fun renderState(state: PlayListState) {
        playlistState.postValue(state)
    }

    companion object {
        private const val DELAY_UPDATE_TIME_MS = 300L

    }

}
