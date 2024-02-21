package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.models.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val urlApi = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunseService = retrofit.create(ApiSong::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest){

            val resp = iTunseService.search(dto.text).execute()

            val body = resp.body()?:Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode == 400 }
        }
    }


}