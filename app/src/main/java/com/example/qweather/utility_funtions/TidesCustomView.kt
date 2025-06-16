package com.example.qweather.utility_funtions

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt

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
        strokeWidth = 2f
    }
    private val bottomLinePaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 2f
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
        color = "#FFD698".toColorInt()
        isAntiAlias = true
        style = Paint.Style.FILL

    }

    private val tidePathPaint = Paint().apply {
        color = "#BF7600".toColorInt()
        strokeWidth = 10f
        isAntiAlias = true
        style = Paint.Style.STROKE


    }

    private val circlePaint = Paint().apply {
        color = "#774B02".toColorInt()
        isAntiAlias = true

    }

    private val circleBorderPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

   private val currentCirclePaint = Paint().apply {
        color = Color.WHITE
       isAntiAlias = true
    }

    private val currentCircleBorderPaint = Paint().apply {
        color = "#C80346".toColorInt()
        strokeWidth = 8f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val tidePath = Path()
    private val circlePath = Path()
    private val currentCirclePath = Path()
    private val hoursTextPath = Path()
    private val timeTextPath = Path()
    private val circleRadius = 20f
    private val currentCircleRadius = 25f
    private val linesPath = Path()
    private val bottomLinePath = Path()
    private val tideBackgroundPath = Path().apply {

    }

//    private val hourlyTideData = listOf(
//        "00.00",
//        "01.00",
//        "02.00",
//        "03.00",
//        "04.00",
//        "05.00",
//        "06.00",
//        "07.00",
//        "08.00",
//        "09.00",
//        "10.00",
//        "11.00",
//        "12.00",
//        "13.00",
//        "14.00",
//        "15.00",
//        "16.00",
//        "17.00",
//        "18.00",
//        "19.00",
//        "20.00",
//        "21.00",
//        "22.00",
//        "23.00"
//    )
//    private val tidesData = listOf(1.2, 2.3, 2.1, 1.8, 1.5, 1.2, 0.1, 1.1, 1.4, 1.7, 2.0, 2.3, 2.5, 2.7, 2.9, 1.1, 1.3, 1.5, 1.7, 1.9, 2.1, 2.3, 2.5, 2.7)

    private var hourlyTideData: List<String> = emptyList()
    private var tidesData: List<Double> = emptyList()
    private var currentTide: Double = 0.0

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



        bottomLinePath.reset()
        bottomLinePath.moveTo(0f, (8.5 * lineGap).toFloat())
        bottomLinePath.lineTo(width.toFloat(), (8.5 * lineGap).toFloat())
        canvas.drawPath(bottomLinePath, bottomLinePaint)

        timeTextPath.reset()

        for (i in hourlyTideData.indices) {
            timeTextPath.moveTo(timeGap * i, (8.8 * lineGap).toFloat())
            timeTextPath.lineTo(width.toFloat(), (8.8 * lineGap).toFloat())
            canvas.drawText(hourlyTideData[i], timeGap * i, (8.8 * lineGap).toFloat(), textPaint)
        }

        tidePath.reset()
        circlePath.reset()
        tideBackgroundPath.reset()
        var previousX = 0f
        var previousY = 0f
        var endingX = 0f
        var endingY =0f
        tideBackgroundPath.moveTo(0f+(circleRadius+5f), (8.5 * lineGap).toFloat() )

        for (i in tidesData.indices) {

            circlePath.addCircle(
                (timeGap * i)+(circleRadius+5f),
                (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat(),
                circleRadius,
                Path.Direction.CW
            )
            if(i==0){
                tideBackgroundPath.lineTo((timeGap * i)+(circleRadius+5f), (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat())

                previousX = (timeGap * i)+(circleRadius+5f)
                previousY = (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()
                canvas.drawPath(circlePath, circlePaint)
                canvas.drawPath(circlePath, circleBorderPaint)
                canvas.drawText(tidesData[i].toString(), (timeGap * i), (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-35f, textPaint)
                continue
            }
            if(i==tidesData.size-1){
                endingX = (timeGap * i)+(circleRadius+5f)
                endingY = (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()
            }

            tideBackgroundPath.lineTo((timeGap * i)+(circleRadius+5f), (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat())
            tidePath.moveTo(previousX, previousY)
            tidePath.lineTo((timeGap * i)+(circleRadius+5f), (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat())
            tidePath.close()
            canvas.drawPath(tidePath, tidePathPaint)
            canvas.drawText(tidesData[i].toString(), (timeGap * i), (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-35f, textPaint)

            circlePath.addCircle(
                (timeGap * i)+(circleRadius+5f),
                (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat(),
                circleRadius,
                Path.Direction.CW
            )

            previousX = (timeGap * i)+(circleRadius+5f)
            previousY = (8.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()

        }
        tideBackgroundPath.lineTo(endingX, (8.5 * lineGap).toFloat())
        tideBackgroundPath.close()
        canvas.drawPath(tideBackgroundPath, tideBackgroundPaint)
        canvas.drawPath(circlePath, circlePaint)
        canvas.drawPath(circlePath, circleBorderPaint)


        hoursTextPath.reset()
        hoursTextPath.moveTo(0f, (8.4 * lineGap).toFloat())
        hoursTextPath.lineTo(width.toFloat(), (8.4 * lineGap).toFloat())
        canvas.drawText("Hours", 0f, (8.4 * lineGap).toFloat(), hourTextPaint)

        linesPath.reset()
        linesPath.moveTo(0f, lineGap)
        linesPath.lineTo(width.toFloat(), lineGap)
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

        currentCirclePath.addCircle(
            (timeGap * 12)+(currentCircleRadius+5f),
            (8.5 * lineGap).toFloat() - (currentTide * lineGap).toFloat(),
            currentCircleRadius,
            Path.Direction.CW
        )
        canvas.drawPath(currentCirclePath, currentCirclePaint)
        canvas.drawPath(currentCirclePath, currentCircleBorderPaint)

    }
    fun setTideData(hours: List<String>, tideValues: List<Double>, currentHeight: Double?) {
        this.hourlyTideData = hours
        this.tidesData = tideValues
        if (currentHeight != null) {
            this.currentTide = currentHeight
        }
        requestLayout()
        invalidate()
    }



}

