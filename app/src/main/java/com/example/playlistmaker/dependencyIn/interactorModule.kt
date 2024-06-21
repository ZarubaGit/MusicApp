package com.example.playlistmaker.dependencyIn

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlistmaker.domain.favoriteTrack.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.impl.PlayListInteractorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

//внедрение зависимостей с помощью DI и Koin

val interactorModule = module {


    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    single<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }




}