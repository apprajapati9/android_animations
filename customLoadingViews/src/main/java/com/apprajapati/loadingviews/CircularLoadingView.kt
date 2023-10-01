package com.apprajapati.loadingviews

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.updateLayoutParams

class CircularLoadingView(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val centerXY = PointF(0f, 0f)

    var minHeight = 0f //Minimum height and width of the view.
        get() = field
        set(value) {
            field = value  //value can only be changed when that's above 100.
        }
    var radius = 0f
    var outerCircleRadius = 0f

    var circleColor = Color.WHITE
    var animationDuration = 1000
    var circleWidth = 15f

    private var mCirclePaint = Paint()
    private var outerCirclePaint = Paint()
    private var shownOuter = false

    private val handler = Handler(Looper.getMainLooper())

    private var valueAnimator = ValueAnimator()

    init {
        val setValues =
            context.obtainStyledAttributes(attributeSet, R.styleable.CircularLoadingView, 0, 0)
        animationDuration =
            setValues.getInteger(R.styleable.CircularLoadingView_duration, animationDuration)
        // circleColor = setValues.getColor(R.styleable.CircularLoadingView_circleColor, Color.BLACK)
        circleWidth = setValues.getFloat(R.styleable.CircularLoadingView_circleWidth, circleWidth)
        setValues.recycle()

        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style =
            Paint.Style.STROKE //stroke will make one layer only, fill will draw a filled circle.
        mCirclePaint.color = circleColor
        mCirclePaint.strokeWidth = circleWidth

        outerCirclePaint = Paint()
        outerCirclePaint.isAntiAlias = true
        outerCirclePaint.style =
            Paint.Style.STROKE //stroke will make one layer only, fill will draw a filled circle.
        outerCirclePaint.color = ContextCompat.getColor(context, R.color.black_fade)
        outerCirclePaint.alpha = 100
        outerCirclePaint.strokeWidth = circleWidth / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //centerXY.getCenterXY() //at this point in this method doesn't give the correct center points.
        outerCircleRadius = minHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //Always pick the minimum value as a radius so full circle can be seen.
        if (w < h) {
            minHeight = w.toFloat()
                .div(2) - circleWidth / 2  //-circleWidth so it doesn't get cropped when width is too big.
        } else {
            minHeight = h.toFloat().div(2) - circleWidth / 2
        }

        if (w == h) {
            minHeight = w.toFloat().div(2) - circleWidth / 2
        }
        centerXY.getCenterXY()
        animateProgress()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerXY.x, centerXY.y, radius, mCirclePaint)

        if (shownOuter.not()) {
            canvas.drawCircle(centerXY.x, centerXY.y, minHeight, outerCirclePaint)
        }
    }

    fun PointF.getCenterXY() {
        x = width.toFloat().div(2)
        y = height.toFloat().div(2)
    }

    private fun getColorRange(): PropertyValuesHolder {
        if (circleColor == Color.BLACK) {
            return PropertyValuesHolder.ofInt("colorValues", 0, 255)
        } else {
            return PropertyValuesHolder.ofInt("colorValues", 255, 210, 200, 0)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun setVisibility(visibility: Int) {
        if(visibility == GONE || visibility == INVISIBLE){
            stopAnimation()
        }else{
            animateProgress()
        }
        super.setVisibility(visibility)
    }

    fun stopAnimation(){
        if(valueAnimator != null){
            valueAnimator.cancel()
            valueAnimator.end()
        }
    }


    fun animateProgress() {
       // stopAnimation()
        val valuesHolder = PropertyValuesHolder.ofFloat("progressValue", 0f, minHeight)

        val colorValues = getColorRange() //0 being black and 255 being white.

        handler.postDelayed({
            shownOuter = true
        }, 300)

        valueAnimator = ValueAnimator().apply {
            setValues(valuesHolder, colorValues)
            duration = animationDuration.toLong()
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

}