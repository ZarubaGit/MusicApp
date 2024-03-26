package com.example.playlistmaker.domain.setting.impl

import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {//внедрение зависимостей с помощью DI и Koin
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(setting: ThemeSettings) {
        settingsRepository.updateThemeSetting(setting)
    }

}
