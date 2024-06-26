package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.models.PlayList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListMapper {

    private val gson = Gson()

    fun map(playList: PlayListEntity): PlayList {
        var tracks: ArrayList<Int> = ArrayList()
        if ( playList.tracks is String) {
            tracks = convertFromGson(playList.tracks)
        }
        return PlayList(
            id = playList.id,
            name = playList.name,
            description = playList.description,
            uri = playList.uri,
            tracks = tracks,
            trackTimerMillis = playList.tracksTimerMillis
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            id = playList.id,
            name = playList.name,
            description = playList.description,
            uri = playList.uri,
            tracks = convertToGson(playList.tracks),
            tracksTimerMillis = playList.trackTimerMillis

        )
    }

    private fun convertFromGson(json: String): ArrayList<Int> {
        val itemType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, itemType)
    }

    private fun convertToGson(tracksId: ArrayList<Int>):String {
        val itemType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.toJson(tracksId, itemType)
    }
}