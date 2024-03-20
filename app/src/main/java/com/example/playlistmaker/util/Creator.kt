package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {


    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(context = context))
    }

    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository = provideSettingsRepository(context = context))
    }

    private fun provideSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(context = context)
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(externalNavigator = provideExternalNavigator(context))
    }

    private fun provideExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context = context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context = context)
    }

}