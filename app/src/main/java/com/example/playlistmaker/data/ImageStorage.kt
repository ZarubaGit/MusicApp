package com.example.playlistmaker.data

interface ImageStorage {
    fun saveImageInPrivateStorage(uri: String): String
}