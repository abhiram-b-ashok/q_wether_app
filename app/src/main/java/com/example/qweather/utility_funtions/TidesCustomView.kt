package com.example.qweather.utility_funtions

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class TidesCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val lineGap = 150f
    val timeGap = 200f

    private val linesPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#C4C4C4")
        style = Paint.Style.STROKE
        strokeWidth =2f
    }
    private val bottomLinePaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth =2f
    }

    private val hourTextPaint = Paint().apply {

        color = Color.BLACK
        textSize = 50f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        isAntiAlias = true

    }

    private val tideBackgroundPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }

    private val tidePathPaint = Paint().apply {
        color = Color.YELLOW
        strokeWidth = 4f
        isAntiAlias = true

    }

    private val circlePaint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true

    }

    private val tidePath = Path()
    private val circlePath = Path()
    private val hoursTextPath = Path()
    private val timeTextPath = Path()
    private val tideTextPath = Path()
    private val circleRadius = 10f
    private val linesPath = Path()
    private val bottomLinePath = Path()

    private val hourlyTideData = listOf("00.00", "01.00","02.00","03.00","04.00","05.00","06.00","07.00","08.00","09.00","10.00","11.00","12.00","13.00","14.00","15.00","16.00","17.00","18.00","19.00","20.00","21.00","22.00","23.00")
    private val barWidth = 50.dpToPx()
    private val barSpacing = 16.dpToPx()
    private val totalPaddingHorizontal = 32.dpToPx()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = if (hourlyTideData.isNotEmpty()) {
            (hourlyTideData.size * barWidth) + ((hourlyTideData.size - 1) * barSpacing)
        } else {
            totalPaddingHorizontal
        }
        val desiredHeight = 300.dpToPx()

        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)

        linesPath.reset()
        linesPath.moveTo(0f, lineGap)
        linesPath.lineTo(width.toFloat(),lineGap)
        linesPath.moveTo(0f, 2 * lineGap)
        linesPath.lineTo(width.toFloat(), 2 * lineGap)
        linesPath.moveTo(0f, 3 * lineGap)
        linesPath.lineTo(width.toFloat(), 3 * lineGap)
        linesPath.moveTo(0f, 4 * lineGap)
        linesPath.lineTo(width.toFloat(), 4 * lineGap)
        linesPath.moveTo(0f, 5 * lineGap)
        linesPath.lineTo(width.toFloat(), 5 * lineGap)
        linesPath.moveTo(0f, 6 * lineGap)
        linesPath.lineTo(width.toFloat(), 6 * lineGap)
        linesPath.moveTo(0f, 7 * lineGap)
        linesPath.lineTo(width.toFloat(), 7 * lineGap)
        linesPath.moveTo(0f, 8 * lineGap)
        linesPath.lineTo(width.toFloat(), 8 * lineGap)

        canvas.drawPath(linesPath, linesPaint)
        bottomLinePath.reset()
        bottomLinePath.moveTo(0f, (8.5 *lineGap).toFloat())
        bottomLinePath.lineTo(width.toFloat(), (8.5 *lineGap).toFloat())
        canvas.drawPath(bottomLinePath, bottomLinePaint)

        circlePath.reset()
        circlePath.addCircle(
            width / 4f,
            height / 4f,
            circleRadius,
            Path.Direction.CW)
        canvas.drawPath(circlePath, circlePaint)

        tidePath.reset()
        tidePath.moveTo(0f, height / 2f)
        tidePath.lineTo(width.toFloat(), height / 2f)
        canvas.drawPath(tidePath, tidePathPaint)

        hoursTextPath.reset()
        hoursTextPath.moveTo(0f, (8.4 *lineGap).toFloat())
        hoursTextPath.lineTo(width.toFloat(), (8.4 *lineGap).toFloat())
        canvas.drawText("Hours", 0f,(8.4 *lineGap).toFloat(), hourTextPaint)

        timeTextPath.reset()

        for (i in hourlyTideData.indices) {
            timeTextPath.moveTo(timeGap*i, (8.8 *lineGap).toFloat())
            timeTextPath.lineTo(width.toFloat(), (8.8 *lineGap).toFloat())
            canvas.drawText(hourlyTideData[i], timeGap*i,(8.8 *lineGap).toFloat(), textPaint)
        }

        tideTextPath.reset()

        for (i in hourlyTideData.indices) {
            tideTextPath.moveTo(timeGap*i, (8 *lineGap).toFloat())
            tideTextPath.lineTo(width.toFloat(), (8.8 *lineGap).toFloat())
            canvas.drawText("Tide", 0f, (8.8 *lineGap).toFloat(), textPaint)
        }
        
    }

}