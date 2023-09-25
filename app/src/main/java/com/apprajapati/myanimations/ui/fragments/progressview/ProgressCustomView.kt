package com.apprajapati.myanimations.ui.fragments.progressview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.apprajapati.myanimations.R

class ProgressCustomView(context: Context, attrs: AttributeSet): View(context, attrs) {

    companion object{
        const val full_rotation = 360f
        const val percentage_div= 100f
        const val percent_value_holder= "percent"
    }

    private val ovalShape = RectF()

    private var currentPercentage = 0

    private val parentArcColor = context.resources?.getColor(R.color.primary_light, null) ?: Color.GRAY
    private val fillerArcColor= context.resources?.getColor(R.color.design_default_color_primary_dark, null) ?: Color.BLUE

    private val parentArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = parentArcColor
        strokeWidth = 40f
    }

    private val fillerArcPaint= Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = fillerArcColor
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND //this makes the corners round on circle.
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        setSpace()
        canvas.let {
            drawBackgroundArc(it)
            drawInnerArc(it)
        }
        canvas.drawPoint(width.div(2).toFloat(), height.div(2).toFloat(), parentArcPaint)
    }

    private fun drawBackgroundArc(it: Canvas){
        it.drawArc(ovalShape, 0f, 360f, true, parentArcPaint)
    }

    private fun drawInnerArc(it: Canvas){
        val percentFill = getCurrentPercentageToFill()
        it.drawArc(ovalShape, 270f, percentFill, false, fillerArcPaint)
    }

    private fun getCurrentPercentageToFill() =
        (360f * (currentPercentage/ percentage_div)) // formula- per*100/value. , divide by 100 and then multipy by value.

    fun animateProgress(){
        val valuesHolder= PropertyValuesHolder.ofFloat(percent_value_holder, 0f, 100f)

        val animator= ValueAnimator().apply {
            setValues(valuesHolder)
            duration = 5000
            interpolator = AccelerateInterpolator()

            addUpdateListener {
                val percentage = it.getAnimatedValue(percent_value_holder) as Float
                currentPercentage = percentage.toInt()
                invalidate()
            }
        }
        animator.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        animateProgress()
        return super.onTouchEvent(event)
    }



    private fun setSpace(){
        val HCenter = width.div(2).toFloat()
        val VCenter = height.div(2).toFloat()
        val ovalSize= 300
        ovalShape.set(HCenter - ovalSize, VCenter - ovalSize, HCenter+ ovalSize, VCenter+ ovalSize)
    }
}