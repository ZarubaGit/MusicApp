package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {//внедрение зависимостей с помощью DI и Koin

    override fun search(expression: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.search(expression).map{ result ->
            when(result) {
                is Resource.Success -> { Pair(result.data, null) }
                is Resource.Error -> { Pair(null, result.message) }
            }
        }
    }
}