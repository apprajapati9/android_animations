package com.apprajapati.myanimations.ui.fragments.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.apprajapati.myanimations.util.pxToDp

import kotlin.math.cos
import kotlin.math.sin

class Circle(
    val radius: Float,
    var mSetcolor: Int = Color.RED,
    var centerPoint: PointF,
    var area: RectF
) {

    val mStrokeWidth = 1f
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = mStrokeWidth
        color = mSetcolor
    }

    var mMovePoint = PointF(0f, 0f)
    var mCurrentAngle = 270f

    var mPathEffect = ComposePathEffect(CornerPathEffect(200f), DiscretePathEffect(2f, 30f))
    fun incrementAngle() {
        if (mCurrentAngle >= 360) {
            mCurrentAngle = 0f
        }
        mCurrentAngle += 1f  //6 to have 60 seconds in 360 degree circle.
    }

    fun draw(canvas: Canvas) {
        //canvas.save()
        // canvas.translate(100f, 100f)
        //paint.pathEffect = ComposePathEffect(CornerPathEffect(10000f), DiscretePathEffect(100f, 50f))
        paint.color = Color.GREEN
        paint.pathEffect = PathEffect()
        // paint.style = Paint.Style.FILL
        paint.style = Paint.Style.STROKE
        //canvas.drawCircle(centerPoint.x, centerPoint.y, radius, paint)
        paint.style = Paint.Style.FILL
        paint.pathEffect = PathEffect()
        canvas.drawCircle(mMovePoint.x, mMovePoint.y, 40f, paint) // Earth
        paint.pathEffect = PathEffect()
        //canvas.drawLine(centerPoint.x, centerPoint.y, mMovePoint.x, mMovePoint.y, paint)
        paint.color = Color.parseColor("#e25822")
        paint.pathEffect = mPathEffect
        canvas.drawCircle(centerPoint.x, centerPoint.y, 100f, paint) //Sun
        // canvas.restore()


       // canvas.drawCircle(mMovePoint.x, mMovePoint.y, 60f, paint )
    }

    fun moveLine() {
        incrementAngle()
        mMovePoint = getXYPointOfAngle(mCurrentAngle, radius.toDouble())
        Log.d("Ajay", "moveLine()- currentAngle= $mCurrentAngle")
        Log.d("Ajay", "moveLine() - currentPoint x = ${mMovePoint.x}, y= ${mMovePoint.y}")
    }

    private fun getXYPointOfAngle(currentAngle: Float, radius: Double): PointF {
        // val startingAngle = Math.toRadians(startFromAngle.toDouble()) //angle by default goes anti-clockwise, by starting from 180, starting 0 degree from left side of the screen, turning into clockwise.
        val angle = Math.toRadians(currentAngle.toDouble())
        val myPoint = PointF(0f, 0f)

        myPoint.y =
            ((radius * sin(angle).toFloat()) + area.height() / 2).toFloat() //think radius as hypotenuse
        myPoint.x = ((radius * cos(angle).toFloat()) + area.width() / 2).toFloat()
        //mathematically reading as - from Height/2, width/2, meaning from the center, point me to the radius(hypotenuse) distance at this given angle sin(angle)/cos(angle)

        Log.d("Ajay", "x= ${myPoint.x}, y= ${myPoint.y}")
        Log.d(
            "Ajay",
            "angle- $angle, sin= ${Math.sin(angle.toDouble())}, cos= ${Math.cos(angle.toDouble())}"
        )
        return myPoint
    }
}

/*
This view is created to achieve solar system animation to demonstrate custom view and animation.
 */
class TheSolarSystemView (context: Context, attributeSet: AttributeSet ?= null) : View(context, attributeSet), Runnable {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    val centerPoint = PointF(0f, 0f)
    var circle: Circle? = null
    var radius = 500f
    val dotRadius = 10f

    var startThread = false
    var callerThread: Thread? = null

    fun PointF.getCenterXY() {
        x = width / 2.toFloat()
        y = height / 2.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


       // radius = width - 20f
        centerPoint.getCenterXY()
        Log.d("Ajay", "radius -> ${pxToDp(context, radius)}")
        val area = RectF(0f, 0f, width.toFloat(), height.toFloat())

        circle = Circle(radius, Color.GREEN, centerPoint, area)
    }



    override fun onDraw(canvas: Canvas) {
        // super.onDraw(canvas)

        canvas.drawColor(Color.BLACK)
//        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, paint )
//
//
//        val pointOfThisAngle = getXYPointOfAngle(180, 90, radius.toDouble()) //Angle goes from left to downward towards Y coordinates, 0..360 - from left to downward and up 360.
//
//        canvas.drawCircle(pointOfThisAngle.x, pointOfThisAngle.y, dotRadius, paint)
//
//        canvas.drawLine(centerPoint.x, centerPoint.y, pointOfThisAngle.x, pointOfThisAngle.y, paint)
//
//        canvas.drawCircle(centerPoint.x, centerPoint.y, dotRadius, paint) //drawing dot on centerpoint


        circle?.draw(canvas)

//        circle?.centerPoint?.x = centerPoint.x
//        circle?.centerPoint?.y = centerPoint.y
        // circle?.draw(canvas)
        // invalidate()
        //testRectangleOnTopOfCircle(canvas)
    }


    private fun getXYPointOfAngle(startFromAngle: Int, angle: Int, radius: Double): PointF {
        val startingAngle =
            Math.toRadians(startFromAngle.toDouble()) //angle by default goes anti-clockwise, by starting from 180, starting 0 degree from left side of the screen, turning into clockwise.
        val angle = startingAngle + Math.toRadians(angle.toDouble())
        val myPoint = PointF(0f, 0f)


        myPoint.y =
            ((radius * sin(angle).toFloat()) + height / 2).toFloat() //think radius as hypotenuse
        myPoint.x = ((radius * cos(angle).toFloat()) + width / 2).toFloat()
        //mathematically reading as - from Height/2, width/2, meaning from the center, point me to the radius(hypotenuse) distance at this given angle sin(angle)/cos(angle)

        Log.d("Ajay", "x= ${myPoint.x}, y= ${myPoint.y}")
        Log.d("Ajay", "angle- $angle, sin= ${Math.sin(angle)}, cos= ${Math.cos(angle)}")
        return myPoint
    }

    /*
        Use this method to test drawing rectangle on top of a circle and x,y calculations based on circle information.
     */
    fun testRectangleOnTopOfCircle(canvas: Canvas) {
        //starting angle must be 0, because calculations of topX/Y are done according to that.

        val radius = 400f
        //   paint.style = Paint.Style.STROKE
        val pointTop = getXYPointOfAngle(0, 270, radius.toDouble()) //top
        val pointleft = getXYPointOfAngle(0, 180, radius.toDouble()) //left
        val pointRight = getXYPointOfAngle(0, 0, radius.toDouble()) // right

        val topY = pointTop.y
        val topX = pointleft.x
        val rectF = RectF(topX, topY, topX + radius * 2, topY + radius * 2)

        canvas.drawRect(rectF, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startThread()
    }

    fun startThread() {
        if (!startThread) {
            startThread = true
            callerThread = Thread(this)
            callerThread!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        stopThread()
        super.onDetachedFromWindow()
    }

    fun stopThread() {
        if (startThread) {
            startThread = false
            //callerThread!!.interrupt()
            callerThread = null
        }
    }

    override fun run() {
        while (startThread) {
            circle?.moveLine()
            postInvalidate()
            try {
                Thread.sleep(40)
                Log.d("Ajay", "thread name= ${Thread.currentThread().name}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //Log.d("Ajay", "run() startThread=$startThread")
    }

}
