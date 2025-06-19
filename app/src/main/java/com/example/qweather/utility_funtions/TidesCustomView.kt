package com.example.qweather.utility_funtions

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TidesCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var showCurrentTide: Boolean = true
    val lineGap = 150f
    val timeGap = 200f
    val tideExtraHeight = 250f

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

    private val hourlyTideData = listOf(
        "00.00",
        "01.00",
        "02.00",
        "03.00",
        "04.00",
        "05.00",
        "06.00",
        "07.00",
        "08.00",
        "09.00",
        "10.00",
        "11.00",
        "12.00",
        "13.00",
        "14.00",
        "15.00",
        "16.00",
        "17.00",
        "18.00",
        "19.00",
        "20.00",
        "21.00",
        "22.00",
        "23.00"
    )
//    private val tidesData = listOf(1.2, 2.3, 2.1, 1.8, 1.5, 1.2, 0.1, 1.1, 1.4, 1.7, 2.0, 2.3, 2.5, 2.7, 2.9, 1.1, 1.3, 1.5, 1.7, 1.9, 2.1, 2.3, 2.5, 2.7)

//    private var hourlyTideData: List<String> = emptyList()
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
        bottomLinePath.moveTo(0f, (6.5 * lineGap).toFloat())
        bottomLinePath.lineTo(width.toFloat(), (6.5 * lineGap).toFloat())
        canvas.drawPath(bottomLinePath, bottomLinePaint)

        timeTextPath.reset()

        for (i in hourlyTideData.indices) {
            timeTextPath.moveTo(timeGap * i, (6.8 * lineGap).toFloat())
            timeTextPath.lineTo(width.toFloat(), (6.8 * lineGap).toFloat())
            canvas.drawText(hourlyTideData[i], timeGap * i, (6.8 * lineGap).toFloat(), textPaint)
        }

        tidePath.reset()
        circlePath.reset()
        tideBackgroundPath.reset()
        var previousX = 0f
        var previousY = 0f
        var endingX = 0f
        var endingY =0f
        tideBackgroundPath.moveTo(0f, (6.5 * lineGap).toFloat() )

        for (i in tidesData.indices) {

            circlePath.addCircle(
                ((timeGap * i)+(circleRadius+5f))+20f,
                (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-tideExtraHeight ,
                circleRadius,
                Path.Direction.CW
            )

            if(i==0){
                tideBackgroundPath.lineTo(0f, (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-tideExtraHeight)
                previousX = ((timeGap * i)+(circleRadius+5f))+20f
                previousY = (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat() -tideExtraHeight
                canvas.drawPath(circlePath, circlePaint)
                canvas.drawPath(circlePath, circleBorderPaint)
                canvas.drawText(tidesData[i].toString(), (timeGap * i)+20f, (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-35f-tideExtraHeight, textPaint)
                continue
            }

            if(i==tidesData.size-1){
                endingX = ((timeGap * i)+(circleRadius+5f))+100f
                endingY = (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat() -tideExtraHeight
            }

            if(tidesData[i]>3.5){
                tidePath.moveTo(previousX, previousY)
                tidePath.lineTo((timeGap * i)+(circleRadius+5f)+20f, lineGap-35f)
                tideBackgroundPath.lineTo((timeGap * i)+(circleRadius+5f)+20f,lineGap-35f)
                circlePath.addCircle(
                    ((timeGap * i)+(circleRadius+5f))+20f,
                    lineGap-35f ,
                    circleRadius,
                    Path.Direction.CW
                )
                canvas.drawText(tidesData[i].toString(), (timeGap * i), lineGap-65f, textPaint)
                previousX = ((timeGap * i)+(circleRadius+5f))+20f
                previousY = lineGap-35f

            }

            tideBackgroundPath.lineTo((timeGap * i)+(circleRadius+5f)+20f, (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-tideExtraHeight)
            tidePath.moveTo(previousX, previousY)
            tidePath.lineTo((timeGap * i)+(circleRadius+5f)+20f, (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-tideExtraHeight)
            tidePath.close()
            canvas.drawPath(tidePath, tidePathPaint)
            canvas.drawText(tidesData[i].toString(), (timeGap * i), (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-35f-tideExtraHeight, textPaint)

            previousX = (timeGap * i)+(circleRadius+5f)+20f
            previousY = (6.5 * lineGap).toFloat() - (tidesData[i] * lineGap).toFloat()-tideExtraHeight

        }
        tideBackgroundPath.lineTo(endingX, previousY)
        tideBackgroundPath.lineTo(endingX, (6.5 * lineGap).toFloat())
        tideBackgroundPath.close()
        canvas.drawPath(tideBackgroundPath, tideBackgroundPaint)
        canvas.drawPath(circlePath, circlePaint)
        canvas.drawPath(circlePath, circleBorderPaint)

        hoursTextPath.reset()
        hoursTextPath.moveTo(0f, (6.4 * lineGap).toFloat())
        hoursTextPath.lineTo(width.toFloat(), (6.4 * lineGap).toFloat())
        canvas.drawText("Hours", 0f, (6.4 * lineGap).toFloat(), hourTextPaint)

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
        canvas.drawPath(linesPath, linesPaint)


        if (showCurrentTide) {
            currentCirclePath.reset()

            val currentTideHourIndex = 12

            if (currentTideHourIndex < hourlyTideData.size && currentTideHourIndex >= 0) {
                val currentCircleX = (timeGap * currentTideHourIndex) + (currentCircleRadius) + 20f
                val currentCircleY = ((6.5 * lineGap) - (currentTide * lineGap)) + 10f  -tideExtraHeight

                currentCirclePath.addCircle(
                    currentCircleX,
                    currentCircleY.toFloat(),
                    currentCircleRadius,
                    Path.Direction.CW
                )
                canvas.drawPath(currentCirclePath, currentCirclePaint)
                canvas.drawPath(currentCirclePath, currentCircleBorderPaint)

                canvas.drawText(
                    "Current Tide: $currentTide",
                    currentCircleX - (hourTextPaint.measureText("Current Tide: $currentTide") / 2),
                    (currentCircleY - currentCircleRadius - 15f).toFloat(),
                    textPaint
                )
            }
            //hiding current tide indicator if the index is not 0
            else if (tidesData.isNotEmpty()) { // Fallback if index is bad but data exists
                // Fallback positioning, e.g., middle of the view or based on available data
                val fallbackX = width / 2f
                val fallbackY = ((6.5 * lineGap) - (currentTide * lineGap)) + 10f
                currentCirclePath.addCircle(
                    fallbackX,
                    fallbackY.toFloat() ,
                    currentCircleRadius,
                    Path.Direction.CW
                )
                canvas.drawPath(currentCirclePath, currentCirclePaint)
                canvas.drawPath(currentCirclePath, currentCircleBorderPaint)
                canvas.drawText(
                    "Current Tide: $currentTide",
                    fallbackX - (hourTextPaint.measureText("Current Tide: $currentTide") / 2),
                    (fallbackY - currentCircleRadius - 15f).toFloat(),
                    textPaint
                )
                Log.w("TidesCustomView", "currentTideHourIndex out of bounds, using fallback position.")
            } else {
                Log.w("TidesCustomView", "Cannot draw current tide indicator: No tide data or invalid index.")
            }
        }

    }

    fun setTideData(tideValues: List<Double>, currentHeight: Double?, displayCurrentTide: Boolean) {
        this.tidesData = tideValues
        if (currentHeight != null) {
            this.currentTide = currentHeight
        }
        
        this.showCurrentTide = displayCurrentTide

        requestLayout()
        invalidate()
    }

}


