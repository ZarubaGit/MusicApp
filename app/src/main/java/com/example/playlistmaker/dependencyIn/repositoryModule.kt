package com.example.playlistmaker.dependencyIn

import android.media.MediaPlayer
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.setting.SettingsRepository
import org.koin.dsl.module

//внедрение зависимостей с помощью DI и Koin

val repositoryModule = module {

    factory<AudioPlayerInteractor> {(mediaPlayer: MediaPlayer) ->
        AudioPlayerInteractorImpl(mediaPlayer)
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

}