package com.example.playlistmaker.ui.main.viewModelMain

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.setting.SettingsInteractor

class RootViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {//внедрение зависимостей с помощью DI и Koin

    fun setAppTheme() {
        val systemTheme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(systemTheme)
    }

}