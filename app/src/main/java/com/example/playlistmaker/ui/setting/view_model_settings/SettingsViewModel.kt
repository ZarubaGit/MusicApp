package com.example.playlistmaker.ui.setting.view_model_settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor, private val sharingInteractor: SharingInteractor
) : ViewModel() {
    //внедрение зависимостей с помощью DI и Koin



    fun updateThemeSettings(isChecked: Boolean) {
         if (isChecked) {
            settingsInteractor.updateThemeSetting(ThemeSettings.DARK)
        } else {
            settingsInteractor.updateThemeSetting(ThemeSettings.LIGHT)
        }
    }

    fun shareApp(url: String, title: String) {
        sharingInteractor.shareApp(link = url, title = title)
    }

    fun openSupport(email: String, subject: String, text: String) {
        sharingInteractor.openSupport(email = email, subject = subject, text = text)
    }

    fun openTerms(url: String) {
        sharingInteractor.openTerms(link = url)
    }




}