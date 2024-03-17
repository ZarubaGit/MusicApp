package com.example.playlistmaker.dependencyIn

import com.example.playlistmaker.data.player.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

}