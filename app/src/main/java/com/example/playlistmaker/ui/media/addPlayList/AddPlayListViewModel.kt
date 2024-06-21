package com.example.playlistmaker.ui.media.addPlayList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playList.PlayListInteractor

class AddPlayListViewModel(private val playListInteractor: PlayListInteractor) : ViewModel() {
    private val playList = PlayList(tracks = ArrayList())
    private val playListState = MutableLiveData<PlayList>()

    fun observerPlayListState(): LiveData<PlayList> = playListState

init {
    renderState(playList)
}

    fun addName(text: String) {
        playList.name = text
        renderState(playList)
    }

    fun addDescription(text: String) {
        playList.description = text
        renderState(playList)
    }

    fun addImage(uri: String) {
        playList.uri = uri
        renderState(playList)
    }

    suspend fun savePlaylist() {
        playListInteractor.add(playList)
    }
    private fun renderState(playList: PlayList) {
        playListState.postValue(playList)
    }
}