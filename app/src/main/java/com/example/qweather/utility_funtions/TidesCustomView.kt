package com.example.qweather.utility_funtions

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TidesCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val linesPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 2f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {

    }


}