package com.example.canvasapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.qweather.R
import kotlin.math.cos

class SunRiseCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val diameter = 600f
    private var sweepPercent = 0f
    private lateinit var sweepAnim: ValueAnimator

    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#DCF1FB")
        isAntiAlias = true
    }

    private val dashedPaint = Paint().apply {
        color = Color.parseColor("#0095DA")
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.parseColor("#0095DA")
        isAntiAlias = true
    }

    private val groundPaint = Paint().apply {
        color = Color.parseColor("#D6D6D6")
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val sunPath = Path()
    private val dashedPath = Path()

    private val sunDrawable: Drawable =
        ContextCompat.getDrawable(context, R.drawable.sweep_sun_ic)
            ?: throw IllegalStateException("Drawable sun not found")

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offsetX = (width - diameter) / 2f
        val offsetY = 50f

        val arcRect = RectF(offsetX, offsetY, offsetX + diameter, offsetY + diameter)
        val sweepAngle = (sweepPercent / 100f) * 180f

        // 1. Draw dashed arc
        dashedPath.reset()
        dashedPath.arcTo(arcRect, 180f, 180f)
        canvas.drawPath(dashedPath, dashedPaint)

        // 2. Draw sun path (filled portion)
        sunPath.reset()
        sunPath.arcTo(arcRect, 180f, sweepAngle)
        val unsweptAngle = 180f - sweepAngle
        val extraLength = (cos(Math.toRadians(unsweptAngle.toDouble())) * (diameter / 2f)).toFloat()
        sunPath.lineTo(offsetX + diameter / 2f + extraLength, offsetY + diameter / 2f)
        sunPath.close()
        canvas.drawPath(sunPath, backgroundPaint)

        // 3. Position sun icon along the arc
        val motionPath = Path().apply {
            arcTo(arcRect, 180f, 180f)
        }

        val measure = PathMeasure(motionPath, false)
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        measure.getPosTan(measure.length * (sweepPercent / 100f), pos, tan)

        val sunWidth = sunDrawable.intrinsicWidth
        val sunHeight = sunDrawable.intrinsicHeight
        val sunLeft = (pos[0] - sunWidth / 2).toInt()
        val sunTop = (pos[1] - sunHeight / 2).toInt()
        sunDrawable.setBounds(sunLeft, sunTop, sunLeft + sunWidth, sunTop + sunHeight)
        sunDrawable.draw(canvas)

        // 4. Draw ground and endpoint circles
        canvas.drawLine(
            0f,
            offsetY + diameter / 2f,
            width.toFloat(),
            offsetY + diameter / 2f,
            groundPaint
        )
        canvas.drawCircle(offsetX, offsetY + diameter / 2f, 15f, circlePaint)
        canvas.drawCircle(offsetX + diameter, offsetY + diameter / 2f, 15f, circlePaint)
    }

    fun setSweepAngleAnimator(sweep: Float) {
        val targetSweep = sweep.coerceIn(0f, 100f)
        sweepAnim = ValueAnimator.ofFloat(0f, targetSweep).apply {
            duration = 1000
            addUpdateListener {
                sweepPercent = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

}

