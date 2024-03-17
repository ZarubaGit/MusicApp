package com.example.playlistmaker.domain.setting

import com.example.playlistmaker.domain.setting.model.ThemeSettings

interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings

    fun updateThemeSetting(setting: ThemeSettings)
}