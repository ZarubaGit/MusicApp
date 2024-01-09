import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(context: Context) {

    companion object {
        private const val PREF_NAME = "SearchHistory"
        private const val KEY_HISTORY = "history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getSearchHistory(): MutableList<Track> {
        val historyString = sharedPreferences.getString(KEY_HISTORY, null)
        Log.d("SH", "getSearchHistory: $historyString")
        return if (historyString != null) {
            gson.fromJson(historyString, object : TypeToken<MutableList<Track>>() {}.type)
        } else {
            mutableListOf()
        }
    }

    fun saveTrackToHistory(track: Track) {
        val history = getSearchHistory().toMutableList()
        if (history.none { it.trackId == track.trackId }) {
            history.add(0, track)
            if (history.size > MAX_HISTORY_SIZE) {
                history.removeAt(history.size - 1)
            }
            saveHistoryToPreferences(history)
            Log.d("SearchHistory", "Saved history: $history")
        }
    }

    private fun saveHistoryToPreferences(history: MutableList<Track>) {
        val historyString = gson.toJson(history)
        sharedPreferences.edit().putString(KEY_HISTORY, historyString).apply()
        Log.d("SearchHistory", "Saved history to SharedPreferences")
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }
}