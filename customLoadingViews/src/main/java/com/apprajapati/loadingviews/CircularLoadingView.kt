package com.apprajapati.loadingviews

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class CircularLoadingView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet), Runnable{

    private val centerXY= PointF(0f, 0f)
    private val mCirclePaint = Paint().apply{
        isAntiAlias = true
        style = Paint.Style.STROKE //stroke will make one layer only, fill will draw a filled circle.
        color = Color.BLACK
        strokeWidth = 10f
    }

    var minHeight = 0f //Minimum height and width of the view.
        get() = field
        set(value) {
            field = value  //value can only be changed when that's above 100.
        }

    val viewRect: RectF = RectF()
    var radius = 0f
    var outerCircleRadius = 0f

    var valueAnimator = ValueAnimator()
    var fadeAnimator = ValueAnimator()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //centerXY.getCenterXY() //at this point in this method doesn't give the correct center points.
        //viewRect.set(centerXY.x, centerXY.y, minHeight, minHeight)
        outerCircleRadius = minHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //Always pick the minimum value as a radius so full circle can be seen.
        if(w < h){
            minHeight = w.toFloat().div(2)-5
        }else{
            minHeight = h.toFloat().div(2)-5
        }

        if(w == h){
            minHeight = w.toFloat().div(2)-5
        }

        mCirclePaint.strokeWidth = minHeight/8
        animateProgress()
    }

    override fun onDraw(canvas: Canvas) {
        centerXY.getCenterXY()

        canvas.drawCircle(centerXY.x, centerXY.y, radius, mCirclePaint)
    }

    fun PointF.getCenterXY(){
        x = width.toFloat().div(2) //width/2).toFloat()
        y = height.toFloat().div(2) //height/2).toFloat()
    }

    fun animateProgress(){
        val valuesHolder= PropertyValuesHolder.ofFloat("progressValue", 0f, minHeight)

        val colorValues = PropertyValuesHolder.ofInt("colorValues",  0, 255) //0 being black and 255 being white.

        valueAnimator = ValueAnimator().apply {
            setValues(valuesHolder, colorValues)
            duration = 1200
            repeatCount = ValueAnimator.INFINITE

            interpolator = DecelerateInterpolator(2f) //10f does give some interesting effects

            addUpdateListener {
                val colors = it.getAnimatedValue("colorValues") as Int
                mCirclePaint.color = Color.rgb(colors, colors, colors)

                val percentage = it.getAnimatedValue("progressValue") as Float
                radius = percentage

                invalidate()
            }
        }

        valueAnimator.start()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        valueAnimator.end()
    }
    override fun run() {
        TODO("Not yet implemented")
    }
}