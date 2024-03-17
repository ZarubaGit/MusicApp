package com.example.playlistmaker.domain

sealed class Resource<T>(val data: T? = null, val message: Int? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: Int, data: T? = null): Resource<T>(data, message)
}

enum class LoadingStatus {
    FAILED_SEARCH,
    SUCCESS,
    NO_INTERNET,
    SERVER_ERROR
}