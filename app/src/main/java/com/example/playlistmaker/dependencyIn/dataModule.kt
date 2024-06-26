package com.example.playlistmaker.dependencyIn

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.ImageStorage
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.network.ApiSong
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.storage.ImageStorageImpl
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//внедрение зависимостей с помощью DI и Koin
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

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get ())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "playList.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<ImageStorage>{
        ImageStorageImpl(get())
    }

}