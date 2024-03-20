package com.example.playlistmaker.data.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val ITUNES_URL = "https://itunes.apple.com"

    private val client: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS) // Установка таймаута в 30 секунд
            .build()

        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiSong by lazy {
        client.create(ApiSong::class.java)
    }

    init {
        // Обработка исключений при создании клиента Retrofit
        try {
            client // Инициализация клиента Retrofit
        } catch (e: Exception) {
            // Логирование ошибки
            Log.e("RetrofitClient", "Failed to create Retrofit client", e)
        }
    }
}