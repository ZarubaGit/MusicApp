package com.example.playlistmaker.ui.main.viewModelMain

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.setting.SettingsInteractor

class MainViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {//внедрение зависимостей с помощью DI и Koin

    fun setAppTheme() {
        val systemTheme = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(systemTheme)
    }

}