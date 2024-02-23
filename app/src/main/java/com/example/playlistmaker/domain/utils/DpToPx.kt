package com.example.playlistmaker.domain.utils

import android.content.Context
import android.util.TypedValue

open class DpToPx {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}