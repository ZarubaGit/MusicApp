package com.example.playlistmaker.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

object Changer {

    fun convertMillisToMinutesAndSeconds(millis: Int?): String? {
        if (millis == null) return "00:00"
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }
    fun convertToYear(string: String?): String? {
        return if (string != null) {
            SimpleDateFormat("yyyy", Locale.getDefault()).format(Date.from(Instant.parse(string)))
        } else {
            ""
        }
    }
    fun convertMillisToMinutes(millis: Int): String {
        return SimpleDateFormat("mm", Locale.getDefault()).format(millis)
    }
}