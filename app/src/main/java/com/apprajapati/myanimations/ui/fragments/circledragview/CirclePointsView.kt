package com.apprajapati.myanimations.ui.fragments.circledragview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.apprajapati.myanimations.R



class CirclePointsView(context: Context, attrsSet: AttributeSet) : View(context, attrsSet) {

    companion object {
        val INVALID_VALUE = -1
        const val MAX = 100
        const val MIN = 0

        const val ANGLE_OFFSET= -90f
    }

    var mPoints = MIN
    var mMin = MIN
    var mMax = MAX

    var mStep= 10

    var mIndicatorIcon: Drawable ?= null

    var mProgressWidth = 12f
        get() = field
        set(progress) {
            field = progress
            mProgressPaint.strokeWidth = field
        }
    var mArcWidth = 12f
        get() = field
        set(width){
            field = width
            mArcPaint.strokeWidth = field
        }

    var mClockwise = true
        get() = field
        set(isClockWise) {
            field = isClockWise
        }
    var mEnabled = true
        get() = field
        set(mEnabled) {
            field = mEnabled
        }

    var isMax = false
    var isMin = false

    var mArcRadius = 0f
    val mArcRect : RectF = RectF()
    var mArcPaint: Paint = Paint()

    var mProgressSweep = 0f
    var mProgressPaint: Paint = Paint()

    var mTextSize = 72f
    var mTextPaint: Paint = Paint()
    val mTextRect: Rect = Rect()

    var translateX = 0
    var translateY = 0

    val mIndicatorPoints: PointF = PointF(0f, 0f)

    var mTouchAngle = 0
    var mPointsChangeListener : OnCirclePointsChangeListener ?= null

    //internal vars
    private var mUpdateTimes=0
    private var mPreviousProgress = -1
    private var mCurrentProgress = 0
    init {
        val density = resources.displayMetrics.density

        var arcColor= ContextCompat.getColor(context, R.color.colorAccent)
        var progressColor = ContextCompat.getColor(context, R.color.colorPrimary)
        var textColor = ContextCompat.getColor(context, R.color.white)

        mProgressWidth = (mProgressWidth * density)
        mArcWidth = (mArcWidth * density)
        mTextSize = (mTextSize * density)

        mIndicatorIcon = ContextCompat.getDrawable(context, R.drawable.point_indicator)

        //if(attrsSet != null)
        val setValues = context.obtainStyledAttributes(attrsSet, R.styleable.CirclePointsView, 0, 0)

        val indicatorIcon = setValues.getDrawable(R.styleable.CirclePointsView_indicatorIcon)
        if(indicatorIcon != null){
            mIndicatorIcon = indicatorIcon
        }

        val indicatorIconHalfWidth = mIndicatorIcon!!.intrinsicWidth / 2
        val indicatorIconHalfHeight = mIndicatorIcon!!.intrinsicHeight / 2
        mIndicatorIcon!!.setBounds(-indicatorIconHalfWidth, -indicatorIconHalfHeight, indicatorIconHalfWidth, indicatorIconHalfHeight)

        mPoints = setValues.getInteger(R.styleable.CirclePointsView_points, mPoints)
        mMin = setValues.getInteger(R.styleable.CirclePointsView_min, mMin)
        mMax = setValues.getInteger(R.styleable.CirclePointsView_max, mMax)
        mStep = setValues.getInteger(R.styleable.CirclePointsView_step, mStep)

        mProgressWidth = setValues.getDimension(R.styleable.CirclePointsView_progressWidth, mProgressWidth)
        progressColor = setValues.getColor(R.styleable.CirclePointsView_progressColor, progressColor)

        mArcWidth = setValues.getDimension(R.styleable.CirclePointsView_arcWidth, mArcWidth)
        arcColor = setValues.getColor(R.styleable.CirclePointsView_arcColor, arcColor)

        mTextSize = setValues.getDimension(R.styleable.CirclePointsView_textSize, mTextSize).toInt().toFloat()
        textColor = setValues.getColor(R.styleable.CirclePointsView_textColor, textColor)


        mClockwise = setValues.getBoolean(R.styleable.CirclePointsView_clockwise, mClockwise)
        mEnabled = setValues.getBoolean(R.styleable.CirclePointsView_enabled, mEnabled)

        setValues.recycle()

        // range check

        // range check
        mPoints = if (mPoints > mMax) mMax else mPoints
        mPoints = if (mPoints < mMin) mMin else mPoints

        mProgressSweep = mPoints / valuePerDegree()

        mArcPaint = Paint()
        mArcPaint.color = arcColor
        mArcPaint.isAntiAlias = true
        mArcPaint.style = Paint.Style.STROKE
        mArcPaint.strokeWidth = mArcWidth.toFloat()


        mProgressPaint = Paint()
        mProgressPaint.color = progressColor
        mProgressPaint.isAntiAlias = true
        mProgressPaint.style = Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressWidth.toFloat()


        mTextPaint = Paint()
        mTextPaint.color = textColor
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = mTextSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val min = Math.min(width, height)

        translateX = (width*0.5f).toInt()
        translateY = (height*0.5f).toInt()

        var arcDiameter= min - paddingLeft
        mArcRadius = (arcDiameter/2).toFloat()

        val top = height /2 - (arcDiameter /2)
        val left = width / 2 - (arcDiameter / 2)
        mArcRect!!.set(left.toFloat(), top.toFloat(), left+arcDiameter.toFloat(), top+arcDiameter.toFloat())
        updateIndicatorIconPosition()


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if(!mClockwise){
            canvas.scale(-1f, 1f, mArcRect.centerX(), mArcRect.centerY())
        }

        //drawing text
        val textPoint: String = mPoints.toString()
        mTextPaint.getTextBounds(textPoint, 0, textPoint.length, mTextRect)

        //center the text
        val xPosition = width/2 - mTextRect.width()/2
        val yPosition = ((mArcRect.centerY()) - ((mTextPaint.descent() + mTextPaint.ascent())/2))

        canvas.drawText(textPoint, xPosition.toFloat(), yPosition, mTextPaint)

        //draw arc and progress
        canvas.drawArc(mArcRect, ANGLE_OFFSET, 360f, false, mArcPaint)
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint)

        if(mEnabled){
            //draw indicator
            canvas.translate(translateX- mIndicatorPoints.x, translateY
            - mIndicatorPoints.y)
            mIndicatorIcon!!.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(isEnabled){
            this.parent.requestDisallowInterceptTouchEvent(true)

            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    if(mPointsChangeListener != null){
                        mPointsChangeListener!!.onStartTrackingTouch(this)
                    }
                }

                MotionEvent.ACTION_MOVE ->{
                    updateOnTouch(event)
                }

                MotionEvent.ACTION_UP ->{
                    if(mPointsChangeListener != null){
                        mPointsChangeListener!!.onStopTrackingTouch(this)
                    }
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }

                MotionEvent.ACTION_CANCEL ->{
                    if(mPointsChangeListener != null){
                        mPointsChangeListener!!.onStopTrackingTouch(this)
                    }
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return true
        }
        return false
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if(mIndicatorIcon != null && mIndicatorIcon!!.isStateful){
            val state = drawableState
            mIndicatorIcon!!.setState(state)
        }
        invalidate()
    }

    private fun updateOnTouch(event: MotionEvent){
        isPressed = true
        mTouchAngle = convertTouchEventPointToAngle(event.x, event.y).toInt()
        val progress = convertAngleToProgress(mTouchAngle)
        updateProgress(progress, true)
    }

    fun convertTouchEventPointToAngle(positionX : Float, positionY: Float): Float{
        var x = positionX - translateX
        val y = positionY - translateY

        x = if(mClockwise) x else -x
        var angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble()) + (Math.PI /2))
        angle = if(angle < 0) angle+360 else angle
        return angle.toFloat()
    }

    fun convertAngleToProgress(touchAngle : Int): Int {
        return Math.round(valuePerDegree()*touchAngle)
    }

    private fun updateIndicatorIconPosition(){
        val thumbAngle = (mProgressSweep+90)
        mIndicatorPoints.x = (mArcRadius*Math.cos(Math.toRadians(thumbAngle.toDouble()))).toFloat()
        mIndicatorPoints.y = (mArcRadius* Math.sin(Math.toRadians(thumbAngle.toDouble()))).toFloat()
    }

    private fun valuePerDegree(): Float {
        return mMax/360f
    }

    public interface OnCirclePointsChangeListener{
        fun onPointsChanged(pointsView : CirclePointsView, points: Int, fromUser: Boolean)
        fun onStartTrackingTouch(pointsView: CirclePointsView)
        fun onStopTrackingTouch(pointsView: CirclePointsView)
    }

    private fun updateProgress(progress: Int, fromUser: Boolean){

        var mValueProgress= progress
        val maxDetectValue = mMax*0.95
        val minDetectValue = mMin*0.0f + mMin

        mUpdateTimes++
        if(mValueProgress==INVALID_VALUE){
            return
        }

        //avoid accidentally touch to become max from the orig point
        if(mValueProgress > maxDetectValue && mPreviousProgress == INVALID_VALUE){
            return
        }

        //record prev and curr progress change
        if(mUpdateTimes == 1){
            mCurrentProgress = mValueProgress
        }else{
            mPreviousProgress = mCurrentProgress
            mCurrentProgress = mValueProgress
        }

        mPoints = mValueProgress - (mValueProgress % mStep)

        //determine whether reach max or min to lock point update event
        //when reaching max, the progress will drop from max  and vice versa

        //if reached max or min, stop increasing/decreasing to avoid exceeding the min/max
        if(mUpdateTimes > 1 && !isMin && !isMax) {
            if (mPreviousProgress >= maxDetectValue && mCurrentProgress <= minDetectValue && mPreviousProgress > mCurrentProgress) {
                isMax = true
                mValueProgress = mMax
                mPoints = mMax

                if (mPointsChangeListener != null) {
                    mPointsChangeListener!!.onPointsChanged(this, mValueProgress, fromUser)
                    return
                }
            } else if ((mCurrentProgress >= maxDetectValue && mPreviousProgress <= minDetectValue && mCurrentProgress > mPreviousProgress) || mCurrentProgress <= mMin) {
                isMin = true
                mValueProgress = mMin
                mPoints = mMin

                if (mPointsChangeListener != null) {
                    mPointsChangeListener!!.onPointsChanged(this, mValueProgress, fromUser)
                    return
                }
            }
            invalidate()
        }else{

            if(isMax and (mCurrentProgress < mPreviousProgress) && mCurrentProgress >= maxDetectValue){
                isMax = false
            }
            if(isMin && (mPreviousProgress < mCurrentProgress) &&
                mPreviousProgress <= minDetectValue && mCurrentProgress <= minDetectValue && mPoints >= mMin){
                isMin = false
            }
        }

        if(!isMin && !isMax){
            mValueProgress = if (mValueProgress > mMax) mMax else mValueProgress
            mValueProgress = if (mValueProgress < mMin) mMin else mValueProgress

            if(mPointsChangeListener != null){
                mValueProgress -= (mValueProgress % mStep)
                mPointsChangeListener!!.onPointsChanged(this, mValueProgress, fromUser)
            }
            mProgressSweep = mValueProgress / valuePerDegree()
            updateIndicatorIconPosition()
            invalidate()
        }
    }

    fun setPoints(points: Int){
        var points = points
        points = if (points > mMax) mMax else points
        points = if (points < mMin) mMin else points
        updateProgress(points, false)
    }

    fun setOnPointsChangeListener(listener: OnCirclePointsChangeListener){
        mPointsChangeListener = listener
    }

    fun setStep(step: Int){
        mStep = step
    }
}