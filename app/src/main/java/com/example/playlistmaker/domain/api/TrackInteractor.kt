package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun search(text: String): Flow<Pair<List<Track>?, Int?>>
}