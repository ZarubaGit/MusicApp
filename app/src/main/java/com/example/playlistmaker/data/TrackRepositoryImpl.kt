package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun search(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(text))
        return when (response.resultCode) {
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map { trackDto ->
                    TrackMapper.map(trackDto = trackDto)
                })
            }
            else -> {
                Resource.Error(response.resultCode)
            }
        }
    }
}