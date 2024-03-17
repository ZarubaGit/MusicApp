package com.example.playlistmaker.dependencyIn

import com.example.playlistmaker.presentation.view_model_audio_player.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }
}