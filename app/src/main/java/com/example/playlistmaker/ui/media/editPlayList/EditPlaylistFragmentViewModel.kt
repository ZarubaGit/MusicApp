package com.example.playlistmaker.ui.media.editPlayList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.ui.media.addPlayList.AddPlayListViewModel
import kotlinx.coroutines.launch

class EditPlaylistFragmentViewModel(
    private val playlistInteractor: PlayListInteractor
): AddPlayListViewModel(playlistInteractor) {

    private val playlistData = MutableLiveData<PlayList>()
    fun observePlaylist(): LiveData<PlayList> = playlistData
    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getById(id).collect {
                playlistData.postValue(it)
                super.playList = it
            }
        }
    }
    suspend fun savePlaylist(playlist: PlayList) {
        viewModelScope.launch {
            playlistInteractor.update(playlist)
        }
    }
}