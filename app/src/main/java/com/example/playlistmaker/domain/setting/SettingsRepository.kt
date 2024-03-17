package com.example.playlistmaker.domain.setting

import com.example.playlistmaker.domain.setting.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)

}