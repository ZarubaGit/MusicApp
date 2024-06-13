package com.example.playlistmaker.ui.media.favoriteSongFragmentViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteSongsFragmentViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor):
    ViewModel() {
        private val favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Empty)

    fun observerFavoriteState():LiveData<FavoriteState> = favoriteState

    init {
        getTracks()
    }

    fun getTracks() {
        viewModelScope.launch {
            favoriteTrackInteractor.getAll().collect {track ->
                processResult(track)
            }
        }
    }

private fun processResult(track: List<Track>) {
    if (track.isEmpty()) {
        renderState(FavoriteState.Empty)
    } else {
        for (i in track) {
            i.isFavorite = true
        }
        renderState(FavoriteState.Content(track))
    }
}

private fun renderState(state: FavoriteState) {
    favoriteState.postValue(state)
}
}