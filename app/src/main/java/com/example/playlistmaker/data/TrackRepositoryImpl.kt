package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun search(text: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(text))
        when (response.resultCode) {
            200 -> {
                emit(Resource.Success((response as TrackSearchResponse).results.map { trackDto ->
                    TrackMapper.map(trackDto = trackDto)
                }))
            }
            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }
    }
}