package com.apprajapati.myanimations.ui.fragments.MovingBallCustomView

import ExplodingBouncer
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View


class MovingBallView(context: Context) : View(context), Runnable {

    var viewWidth = 0f
    var viewHeight = 0f

    var isThreadExecuted = false
    var ballMoverThread: Thread?= null

    private val ballBounds = RectF()

    private var ballOnScreen: ExplodingBouncer?= null

    val paint = Paint.ANTI_ALIAS_FLAG.apply {

    }

    init {
        setBackgroundColor(-0xf0701) //very light blue
        //  focusable
        // requestFocus()
    }


    override fun onSizeChanged(currentWitdh: Int, currentHeight: Int, oldWidth: Int, oldHeight: Int) {
        //super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = currentWitdh.toFloat()
        viewHeight = currentHeight.toFloat()

        val bouncingArea = RectF(0f, 0f, viewWidth, viewHeight)
        ballOnScreen = ExplodingBouncer(PointF(viewWidth/2, viewHeight/2), Color.GREEN, bouncingArea)
    }

    fun startAnimationThread(){
        if(ballMoverThread == null){
            isThreadExecuted = true
            ballMoverThread = Thread(this)
            ballMoverThread!!.start()
        }
    }

    fun stopAnimationThread(){
        if(ballMoverThread != null){
            isThreadExecuted = false
            ballMoverThread!!.interrupt()
            ballMoverThread = null
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