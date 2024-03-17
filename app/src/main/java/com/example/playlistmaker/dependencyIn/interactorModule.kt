package com.example.playlistmaker.dependencyIn

import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import org.koin.dsl.module

val interactorModule = module {


    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

}