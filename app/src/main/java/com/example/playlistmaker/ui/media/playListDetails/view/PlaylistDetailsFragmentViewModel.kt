package com.example.playlistmaker.ui.media.playListDetails.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.media.playList.PlayListTrackState
import kotlinx.coroutines.launch

class PlaylistDetailsFragmentViewModel(
    private val playlistInteractor: PlayListInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val playlistData = MutableLiveData<PlayList>()
    fun observePlaylist(): LiveData<PlayList> = playlistData

    private val tracksData = MutableLiveData<PlayListTrackState>()
    fun observeTracks(): LiveData<PlayListTrackState> = tracksData

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getById(id).collect {
                playlistData.postValue(it)
            }
        }
    }

    fun getTracksForPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistTracks(id).collect {
                if (it.isEmpty()) {
                    renderState(PlayListTrackState.Empty)
                }
                else {
                    renderState(PlayListTrackState.Content(it))
                }
            }
        }
    }

    fun shareApp(message: String) {
        sharingInteractor.sharePlaylist(message)
    }

    fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistTrack(playlistId, trackId)
            getPlaylist(playlistId)
            getTracksForPlaylist(playlistId)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.delete(playlistId)
        }
    }

    private fun renderState(state: PlayListTrackState) {
        tracksData.postValue(state)
    }
}