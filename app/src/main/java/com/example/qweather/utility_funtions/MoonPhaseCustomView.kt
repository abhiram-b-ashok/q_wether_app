package com.example.qweather.utility_funtions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class MoonPhaseCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val starsPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val starCount = 200
    private val stars = mutableListOf<Star>()

    private data class Star(
        val x: Float,
        val y: Float,
        val radius: Float
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w > 0 && h > 0 && stars.isEmpty()) {
            initializeStars(w, h)
        }
    }

    private fun initializeStars(viewWidth: Int, viewHeight: Int) {
        stars.clear()
        for (i in 0 until starCount) {
            stars.add(
                Star(
                    x = Random.nextFloat() * viewWidth,
                    y = Random.nextFloat() * viewHeight,
                    radius = Random.nextFloat() * 2.5f + 1f
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        stars.forEach { star ->
            canvas.drawCircle(star.x, star.y, star.radius, starsPaint)
        }
    }
}