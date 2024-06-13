package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient,
    private val appDataBase: AppDataBase) : TrackRepository {
    override fun search(text: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(text))
        when (response.resultCode) {
            200 -> {
                val track = (response as TrackSearchResponse).results.map { trackDto ->
                    TrackMapper.map(trackDto = trackDto)
                }
                val favorites = appDataBase.getFavoriteTrackDao().getId()
                if (favorites != null) {
                    setTracksToFavorite(track, favorites)
                }
                emit(Resource.Success(track))
            }
            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }
    }

    private fun setTracksToFavorite(track: List<Track>, flag: List<Int>) {
        for (i in track) {
            if(i.trackId in flag) {
                i.isFavorite = true
            }
        }
    }
}