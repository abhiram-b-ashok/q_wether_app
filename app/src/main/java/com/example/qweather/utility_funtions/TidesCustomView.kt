package com.example.qweather.utility_funtions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TidesCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val linesPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 1f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 20f
        isAntiAlias = true

    }

    private val tideBackgroundPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    private val tidePathPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 4f
        isAntiAlias = true

    }

    private val circlePaint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true

    }

    private val tidePath = Path()
    private val circlePath = Path()
    private val textPath = Path()
    private val circleRadius = 10f
    private val linesPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        linesPath.reset()
        linesPath.moveTo(0f, height / 2f)
        linesPath.lineTo(width.toFloat(), height / 2f)
        canvas.drawPath(linesPath, linesPaint)

        circlePath.reset()
        circlePath.addCircle(
            width / 2f,
            height / 2f,
            circleRadius,
            Path.Direction.CW)
        canvas.drawPath(circlePath, circlePaint)

        tidePath.reset()
        tidePath.moveTo(0f, height / 2f)
        tidePath.lineTo(width.toFloat(), height / 2f)
        canvas.drawPath(tidePath, tidePathPaint)

        textPath.reset()
        textPath.moveTo(0f, height / 2f)
        textPath.lineTo(width.toFloat(), height / 2f)
        canvas.drawText("Tides", 0f, height / 2f, textPaint)





    }



}