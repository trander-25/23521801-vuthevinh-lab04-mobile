package com.example.tv_favorite_movie

import android.content.Context
import android.util.TypedValue

object Utils {
    fun convertDpToPixel(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}

