package com.example.playlistmaker.domain.setting.model

enum class ThemeSettings(val darkTheme: Int) {
    LIGHT(darkTheme = 1),
    DARK(darkTheme = 2),
    SYSTEM_DEFAULT(darkTheme = -1)
}