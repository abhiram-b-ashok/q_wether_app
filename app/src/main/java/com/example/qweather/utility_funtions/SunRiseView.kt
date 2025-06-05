package com.android.q_weather_application.canvas

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.qweather.R

import kotlin.math.cos

class SunRiseView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val backgroundColor: Paint = Paint()
    private val circleColor: Paint = Paint()
    private val dashedColor: Paint = Paint()
    private val groundColor: Paint = Paint()

    private val sunImgPath : Path = Path()
    private val dashedPath : Path = Path()

    private val diameter = 600f
    private var sweepPercent = 0f

    private var svgDrawable: Drawable

    private lateinit var sweepAnim: ValueAnimator

    init {
        backgroundColor.color = Color.parseColor("#DCF1FB")
        circleColor.color = Color.parseColor("#0095DA")

        dashedColor.apply {
            color = Color.parseColor("#0095DA")
            style = Paint.Style.STROKE
            pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f, 10f), 0f)
            strokeWidth = 4f
            strokeCap = Paint.Cap.ROUND
        }

        groundColor.apply {
            color = Color.parseColor("#D6D6D6")
            strokeWidth = 2f
        }

        svgDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.sun) }!!
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offSetX = (width-diameter)/2
        val offSetY = 50f

        //MARK:- Semi Circle Drawing
        val rectF = RectF(offSetX,offSetY,offSetX+diameter, offSetY+diameter)

        val sweepAngle = (sweepPercent/100) * 180f
        val unSwept = (180f - sweepAngle).toDouble()
        val extraLength = (cos(Math.toRadians(unSwept)) * (diameter/2)).toFloat()

        //MARK:- Dashed Arc Drawing
        dashedPath.reset()
        dashedPath.arcTo(rectF,180f,180f)
        canvas.drawPath(dashedPath, dashedColor )

        //MARK:- Sun path Drawing
        sunImgPath.reset()
        sunImgPath.arcTo(rectF,180f,sweepAngle)
        sunImgPath.lineTo(offSetX + diameter/2 + extraLength, offSetY + diameter/2)
        sunImgPath.close()
        canvas.drawPath(sunImgPath, backgroundColor)

        //MARK:- Sun Path Animation
        val sunPath = Path().apply {arcTo(offSetX, offSetY, offSetX+diameter, offSetY+diameter , 180f, 180f, true) }
        val measurement = PathMeasure(sunPath, false)
        val pathLength = measurement.length
        val distance = pathLength * (sweepPercent / 100)

        //MARK:- Marking the position of the sun in the graph
        val point = FloatArray(2)
        val posTan = FloatArray(2)
        measurement.getPosTan(distance, point, posTan)

        val svgWidth = svgDrawable.intrinsicWidth
        val svgHeight = svgDrawable.intrinsicHeight
        val svgLeft = (point[0] - svgWidth / 2).toInt()
        val svgTop = (point[1] - svgHeight / 2).toInt()
        val svgRight = svgLeft+svgWidth
        val svgBottom = svgTop+svgHeight

        svgDrawable.setBounds(svgLeft, svgTop, svgRight, svgBottom)
        svgDrawable.draw(canvas)
        canvas.drawLine(0f,offSetY+(diameter/2),width.toFloat(),offSetY+(diameter/2), groundColor)
        canvas.drawCircle(offSetX, offSetY+(diameter/2), 15f, circleColor)
        canvas.drawCircle(offSetX+diameter, offSetY+(diameter/2), 15f, circleColor)
    }

    //MARK:- Sweep Animation
    fun setSweepAngleAnimator(sweep: Float){
        val animatorSweep = sweep.coerceIn(0f, 100f)
        sweepAnim = ValueAnimator.ofFloat(0f, animatorSweep)
        sweepAnim.duration = 3000
        sweepAnim.addUpdateListener {
            sweepPercent = sweepAnim.animatedValue as Float
            invalidate()
        }
        sweepAnim.start()
    }

}