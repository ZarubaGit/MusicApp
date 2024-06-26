package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun sharePlaylist(message: String)
    fun shareLink(url: String, title: String)
    fun openLink(url: String)
    fun openEmail(data: EmailData)
}