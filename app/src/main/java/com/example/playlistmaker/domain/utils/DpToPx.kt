package com.example.playlistmaker.domain.utils

import android.content.Context

interface DpToPx {
    fun dpToPx(dp: Float, context: Context): Int
}