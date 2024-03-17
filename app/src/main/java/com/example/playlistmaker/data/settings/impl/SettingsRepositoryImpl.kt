package com.example.playlistmaker.data.settings.impl

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.setting.SettingsRepository
import com.example.playlistmaker.domain.setting.model.ThemeSettings

class SettingsRepositoryImpl(private val context: Context): SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(FILE_FOR_SAVED, Context.MODE_PRIVATE)
    override fun getThemeSettings(): ThemeSettings {
        val theme = sharedPreferences.getInt(DARK_THEME_MODE, -1)
        return getThemeFromInt(theme) ?: ThemeSettings.SYSTEM_DEFAULT
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        if (settings.darkTheme > 0)
            sharedPreferences.edit().putInt(DARK_THEME_MODE, settings.darkTheme).apply()
        setDarkMode(settings)
    }

    private fun setDarkMode(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            when(settings.darkTheme) {
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                2 -> AppCompatDelegate.MODE_NIGHT_YES
                else -> {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
        )
    }

    companion object {
        const val FILE_FOR_SAVED = "file_for_saved"
        const val DARK_THEME_MODE = "setting_saves"
        private val map = ThemeSettings.values().associateBy(ThemeSettings::darkTheme)
        fun getThemeFromInt(type: Int) = map[type]
    }
}