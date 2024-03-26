package com.example.playlistmaker.ui.search.searchViewModel


import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.ui.search.TrackState

class SearchViewModel(private val trackInteractor: TrackInteractor, private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {
    //внедрение зависимостей с помощью DI и Koin
    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

    }

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