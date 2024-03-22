package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun search(text: String): Resource<List<Track>>
}