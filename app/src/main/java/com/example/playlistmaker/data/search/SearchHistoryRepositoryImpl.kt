package com.example.playlistmaker.data.search

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences,
                                  private val gson: Gson,
    private val appDataBase: AppDataBase) : SearchHistoryRepository {//внедрение зависимостей с помощью DI и Koin


    private var tracks = ArrayList<Track>()
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    override fun addTrack(track: Track) {
        tracks = getHistoryData()
        val existingTrackIndex = tracks.indexOfFirst { it.trackId == track.trackId }

        if (existingTrackIndex != -1) {
            // Если трек уже есть в истории, переместить его в верхнюю часть списка
            tracks.removeAt(existingTrackIndex)
            tracks.add(0, track)
        } else {
            // Если трека нет в истории, добавить его в начало списка
            tracks.add(0, track)
        }
        // Обрезать историю, если она превышает максимальный размер
        if (tracks.size > MAX_HISTORY_SIZE) {
            tracks.removeAt(tracks.size - 1)
        }
        saveHistoryToPreferences(tracks)
    }

    override suspend fun getFromHistory(): ArrayList<Track> {
        val history = getHistoryData()
        val favorites = appDataBase.getFavoriteTrackDao().getId()
        if (favorites != null) {
            setFavoritesToTracks(history, favorites)
        }
        return history
    }

    private fun setFavoritesToTracks(tracks: ArrayList<Track>, indicators: List<Int>) {
        for (track in tracks) {
            track.isFavorite = track.trackId in indicators
        }
    }

    private fun getHistoryData(): ArrayList<Track> {
        val historyString = sharedPreferences.getString(KEY_HISTORY, null)
        return if (historyString != null) {
            gson.fromJson(historyString, object : TypeToken<MutableList<Track>>() {}.type)
        } else {
            return ArrayList()
        }
    }

    override fun clearHistory() {
        editor.remove(KEY_HISTORY).apply()
    }

    private fun saveHistoryToPreferences(history: MutableList<Track>) {
        val historyString = gson.toJson(history)
        editor.putString(KEY_HISTORY, historyString).apply()
    }

    companion object {
        const val KEY_HISTORY = "history"
        const val MAX_HISTORY_SIZE = 10
    }

}