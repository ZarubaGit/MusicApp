package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.dependencyIn.dataModule
import com.example.playlistmaker.dependencyIn.interactorModule
import com.example.playlistmaker.dependencyIn.repositoryModule
import com.example.playlistmaker.dependencyIn.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    //внедрение зависимостей с помощью DI и Koin
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}