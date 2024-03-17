package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(url: String, title: String)
    fun openLink(url: String)
    fun openEmail(data: EmailData)
}