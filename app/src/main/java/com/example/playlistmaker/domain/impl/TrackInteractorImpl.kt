package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = java.util.concurrent.Executors.newCachedThreadPool()
    override fun search(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.search(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}