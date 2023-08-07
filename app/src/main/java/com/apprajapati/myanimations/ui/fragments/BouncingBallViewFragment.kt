package com.apprajapati.myanimations.ui.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/*
learning this from https://www.youtube.com/watch?v=ecgvwg-rELc
and  https://rdcworld-android.blogspot.com/2011/
exercise pdf link - http://naturalprogramming.com/for_students/android/android_exercises.pdf
follow these vids, tutorial.
 */

open class GraphicalObject(var objectCenterPoint: PointF, var objectColor: Int = Color.RED) {

    var objectVelocity = 15f

    fun getObjectPosition(): PointF {
        return objectCenterPoint
    }

    fun setColor(newColor: Int) {
        objectColor = newColor
    }

    fun moveRight() {
        //object_center_point.set(object_center_point.x + object_velocity, object_center_point.y)
        objectCenterPoint[objectCenterPoint.x + objectVelocity] =
            objectCenterPoint.y // translated to a[i] = b  -->> a.set(i, b)
    }

    fun moveLeft() {
        objectCenterPoint[objectCenterPoint.x - objectVelocity] = objectCenterPoint.y
    }

    fun moveUp() {
        objectCenterPoint[objectCenterPoint.x] = objectCenterPoint.y - objectVelocity
    }

    fun moveDown() {
        objectCenterPoint[objectCenterPoint.x] = objectVelocity + objectCenterPoint.y
    }

    fun moveThisObject(movementInDirectionX: Float, movementInDirectionY: Float) {
        objectCenterPoint[objectCenterPoint.x + movementInDirectionX] =
            objectCenterPoint.y + movementInDirectionY
    }

    fun moveToPosition(newPositionX: Float, newPositionY: Float) {
        objectCenterPoint[newPositionX] = newPositionY
    }
}

internal open class Bouncer(givenPosition: PointF, givenColor: Int, val bouncingArea: RectF) :
    GraphicalObject(givenPosition, givenColor) {

    var bouncerRadius = 100f

    var bouncerDirection = Math.random() * Math.PI * 2

    fun get_BouncerRadius(): Float {
        return bouncerRadius
    }

    fun shrink() {
        if (bouncerRadius > 5) {
            bouncerRadius -= 3f
        }
    }

    fun enlarge() {
        bouncerRadius = bouncerRadius + 3
    }

    fun larger(){
        bouncerRadius += 150
    }

    fun setRadius(newRadius: Float) {
        if (newRadius > 3) {
            bouncerRadius = newRadius
        }
    }

    fun containsPoint(givenPoint: PointF): Boolean {

        //Here we use the Pythagorean theorem to calculate the distance from the given point to the center point of the ball. See the note at the of this file.

        val distanceToCenter = Math.sqrt(
            Math.pow(objectCenterPoint.x - givenPoint.x.toDouble(), 2.0) +
                    Math.pow(objectCenterPoint.y - givenPoint.y.toDouble(), 2.0)
        )

        return distanceToCenter <= bouncerRadius

    }

    open fun move() {

        //sinY, cosX
        objectCenterPoint.set(
            objectCenterPoint.x + objectVelocity * Math.cos(bouncerDirection).toFloat(),
            objectCenterPoint.y - objectVelocity * Math.sin(bouncerDirection).toFloat()
        )


        if (objectCenterPoint.y - bouncerRadius <= bouncingArea.top) {
            //the bouncer has hit the norther wall of the bouncing area
            bouncerDirection = 2 * Math.PI - bouncerDirection
        }

        if (objectCenterPoint.x - bouncerRadius <= bouncingArea.left) {
            //the westurn wall has been reached
            bouncerDirection = Math.PI - bouncerDirection
        }

        if (objectCenterPoint.y + bouncerRadius >= bouncingArea.bottom) {
            //southern wall has been reached
            bouncerDirection = 2 * Math.PI - bouncerDirection
        }

        if (objectCenterPoint.x + bouncerRadius >= bouncingArea.right) {
            //eastern wall reached
            bouncerDirection = Math.PI - bouncerDirection
        }
    }

    open fun draw(canvas: Canvas) {
        val fillingPaint = Paint().apply {
            style = Paint.Style.FILL
            color = objectColor
        }

        val outlinePaint = Paint().apply {
            style = Paint.Style.STROKE  //default color for a Paint is black
        }

        canvas.drawCircle(objectCenterPoint.x, objectCenterPoint.y, bouncerRadius, fillingPaint)
        canvas.drawCircle(objectCenterPoint.x, objectCenterPoint.y, bouncerRadius, outlinePaint)
    }

}

internal open class RotatingBouncer(
    givenPosition: PointF,
    givenColor: Int,
    givenBouncingArea: RectF
) : Bouncer(givenPosition, givenColor, givenBouncingArea) {

    var currentRotation = 0
    var anotherBallPaint = Paint()

    init {
        anotherBallPaint.color = -0xff8100 // dark green
    }

    override fun move() {
        super.move()

        currentRotation = currentRotation + 2
        if (currentRotation >= 360) {
            currentRotation = 0
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.save()

        canvas.translate(objectCenterPoint.x, objectCenterPoint.y)


        canvas.rotate(currentRotation.toFloat())

        canvas.drawArc(
            RectF(-bouncerRadius, -bouncerRadius, bouncerRadius, bouncerRadius),
            0f,
            90f,
            true,
            anotherBallPaint
        )

        canvas.drawArc(
            RectF(-bouncerRadius, -bouncerRadius, bouncerRadius, bouncerRadius),
            180f,
            90f,
            true,
            anotherBallPaint
        )

        canvas.restore()
    }
}

internal class ExplodingBouncer(givenPosition: PointF, givenColor: Int, givenBouncingArea: RectF) :
    RotatingBouncer(givenPosition, givenColor, givenBouncingArea) {

    companion object {
        const val BALL_ALIVE_AND_WELL = 0
        const val BALL_EXPLODING = 1
        const val BALL_EXPLODED = 2
    }

    var ballState = BALL_ALIVE_AND_WELL

    var explosionColorAlphaValue = 0

    fun explodeBall(){
        ballState = BALL_EXPLODING
        larger()
    }

    fun isExploded() : Boolean{
        return ballState == BALL_EXPLODED
    }

    override fun move() {
        if(ballState == BALL_ALIVE_AND_WELL){
            super.move()
        }
    }

    override fun draw(canvas: Canvas) {
        if(ballState == BALL_ALIVE_AND_WELL){
            super.draw(canvas)
        }else if(ballState == BALL_EXPLODING){
            if(explosionColorAlphaValue > 0xFF){
                ballState = BALL_EXPLODED
            }
            else{

                super.draw(canvas)

                val explosionPaint = Paint().apply {
                    color = Color.YELLOW
                    alpha = explosionColorAlphaValue
                    style = Paint.Style.FILL
                }

                canvas.drawCircle(objectCenterPoint.x, objectCenterPoint.y, bouncerRadius, explosionPaint)

                explosionColorAlphaValue += 4  //decrease transparency
            }
        }
    }
}

class BouncingBallView(context: Context) : View(context), Runnable {

    var viewWidth = 0f
    var viewHeight = 0f

    var isThreadExecuted = false
    var ballMoverTheard: Thread?= null

    private val ballBounds = RectF()

    private var ballOnScreen: ExplodingBouncer?= null

    val paint = Paint.ANTI_ALIAS_FLAG.apply {

    }

    init {
        setBackgroundColor(-0xf0701) //very light blue
        focusable
        requestFocus()
    }


    override fun onSizeChanged(currentWitdh: Int, currentHeight: Int, oldWidth: Int, oldHeight: Int) {
        //super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = currentWitdh.toFloat()
        viewHeight = currentHeight.toFloat()

        val bouncingArea = RectF(0f, 0f, viewWidth, viewHeight)
        ballOnScreen = ExplodingBouncer(PointF(viewWidth/2, viewHeight/2), Color.GREEN, bouncingArea)
    }

    fun startAnimationThread(){
        if(ballMoverTheard == null){
            isThreadExecuted = true
            ballMoverTheard = Thread(this)
            ballMoverTheard!!.start()
        }
    }

    fun stopAnimationThread(){
        if(ballMoverTheard != null){
            isThreadExecuted = false
            ballMoverTheard!!.interrupt()
            ballMoverTheard = null
        }
    }

    override fun run() {
        while (isThreadExecuted){
            if(ballOnScreen != null){
                ballOnScreen!!.move()
            }

            postInvalidate()

            try{
                Thread.sleep(40)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //return super.onTouchEvent(event)
        if(event.action == MotionEvent.ACTION_DOWN){

            val touchedPoint = PointF(event.x, event.y)

            if(ballOnScreen!!.isExploded()){
                val bounceArea = RectF(0f, 0f, viewWidth, viewHeight)

                ballOnScreen = ExplodingBouncer(touchedPoint, Color.GREEN, bounceArea)
            }

            else if( ballOnScreen!!.containsPoint(touchedPoint)){
                ballOnScreen!!.explodeBall()
            }
            //ballOnScreen!!.bigger()
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        if(ballOnScreen != null){
            ballOnScreen!!.draw(canvas)
        }
    }

}

class BouncingBallViewFragment : Fragment() {

    private lateinit var bouncingBallView: BouncingBallView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bouncingBallView = BouncingBallView(requireActivity())

        return bouncingBallView
    }

    override fun onResume() {
        super.onResume()

        bouncingBallView.startAnimationThread()
    }


    override fun onStop() {
        super.onStop()
        bouncingBallView.stopAnimationThread()
    }

}