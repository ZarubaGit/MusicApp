package com.example.playlistmaker.ui.setting.view_model_settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.util.Creator

class SettingsViewModel(private val settingsInteractor: SettingsInteractor, private val sharingInteractor: SharingInteractor
) : ViewModel() {



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