package com.example.playlistmaker.dependencyIn

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.ApiSong
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_URL = "https://itunes.apple.com"

val dataModule = module {
    single<ApiSong> {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiSong::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("playlist_maker", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

}