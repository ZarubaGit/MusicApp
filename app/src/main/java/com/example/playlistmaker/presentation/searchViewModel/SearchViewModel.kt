package com.example.playlistmaker.presentation.searchViewModel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TrackState
import com.example.playlistmaker.util.Creator

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private val trackInteractor by lazy { Creator.provideTracksInteractor(context = getApplication<Application>()) }
    private val searchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(context = getApplication<Application>()) }
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TrackState>()

    fun observeState(): LiveData<TrackState> = stateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun searchRequest(text: String) {
        if (text.isNotEmpty()) {
            renderState(TrackState.Loading)

            trackInteractor.search(text, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: Int?) {
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
            })
        }
    }

    fun getSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getFromHistory())
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