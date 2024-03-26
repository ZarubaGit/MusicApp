package com.example.playlistmaker.dependencyIn

import com.example.playlistmaker.ui.audioPlayer.view_model_audio_player.AudioPlayerViewModel
import com.example.playlistmaker.ui.main.viewModelMain.MainViewModel
import com.example.playlistmaker.ui.search.searchViewModel.SearchViewModel
import com.example.playlistmaker.ui.setting.view_model_settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//внедрение зависимостей с помощью DI и Koin

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel{
        MainViewModel(get())
    }
}