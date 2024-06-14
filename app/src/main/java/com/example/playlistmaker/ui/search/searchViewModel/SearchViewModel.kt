package com.example.playlistmaker.ui.search.searchViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.ui.search.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val trackInteractor: TrackInteractor, private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        searchJob?.cancel()
    }

    fun searchDebounce(changedText: String) {
searchJob?.cancel()
    searchJob = viewModelScope.launch {
        delay(SEARCH_DEBOUNCE_DELAY)
        searchRequest(changedText)
    }
    }

    private fun searchRequest(text: String) {
        if (text.isNotEmpty()) {
            renderState(TrackState.Loading)

            viewModelScope.launch {
                trackInteractor.search(text).collect{ pair ->
                    processResult(pair.first, pair.second)

}
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: Int?) {
        when {
            errorMessage != null -> {
                renderState(TrackState.Error)
            }

            foundTracks.isNullOrEmpty() -> {
                renderState(TrackState.Empty)
            }

            else -> {
                renderState(TrackState.Content(tracks = foundTracks))
            }
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            val history = searchHistoryInteractor.getFromHistory()
            historyLiveData.postValue(history)
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun onClick(track: Track) {
            searchHistoryInteractor.addTrack(track)
    }


    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }
}