package com.example.playlistmaker.ui.media.playListFragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.ui.media.playList.PlayListState
import kotlinx.coroutines.launch

class PlayListFragmentViewModel(private val playListInteractor: PlayListInteractor): ViewModel() {
    private val playlistState = MutableLiveData<PlayListState>(PlayListState.Empty)
    fun observePlaylistState(): LiveData<PlayListState> = playlistState

    init {
        getPlaylists()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playListInteractor.getAll().collect {
                if (it.isEmpty()) {
                    renderState(PlayListState.Empty)
                }
                else {
                    renderState(PlayListState.Content(it))
                }
            }
        }
    }

    private fun renderState(state: PlayListState) {
        playlistState.postValue(state)
    }
}