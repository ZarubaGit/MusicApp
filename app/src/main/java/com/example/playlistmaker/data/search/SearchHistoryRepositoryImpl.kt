package com.example.playlistmaker.data.search

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    override fun addTrack(track: Track) {
        val history = getFromHistory().toMutableList()
        val existingTrackIndex = history.indexOfFirst { it.trackId == track.trackId }

        if (existingTrackIndex != -1) {
            // Если трек уже есть в истории, переместить его в верхнюю часть списка
            history.removeAt(existingTrackIndex)
            history.add(0, track)
        } else {
            // Если трека нет в истории, добавить его в начало списка
            history.add(0, track)
        }
        // Обрезать историю, если она превышает максимальный размер
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }
        saveHistoryToPreferences(history)
    }

    override fun getFromHistory(): ArrayList<Track> {
        val historyString = sharedPreferences.getString(KEY_HISTORY, null)
        return if (historyString != null) {
            gson.fromJson(historyString, object : TypeToken<MutableList<Track>>() {}.type)
        } else {
            return ArrayList()
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }

    private fun saveHistoryToPreferences(history: MutableList<Track>) {
        val historyString = gson.toJson(history)
        sharedPreferences.edit().putString(KEY_HISTORY, historyString).apply()
    }

    companion object {
        const val PREF_NAME = "com.example.playlistmaker.data.dto.SearchHistory"
        const val KEY_HISTORY = "history"
        const val MAX_HISTORY_SIZE = 10
    }

}