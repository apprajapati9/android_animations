package com.apprajapati.myanimations.ui.fragments.drawShowcase;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random
import kotlin.random.nextInt


internal class DrawDemoView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.FILL
        color = Color.BLUE
    }

    val outLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.BLUE
        textSize = 50f
        strokeWidth = 2f
    }

    val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Style.STROKE
        color = Color.BLUE
        strokeWidth = 15f
    }

    //Drawing demo variables
    var fillRectF = RectF()
    var outerRectF = RectF()
    var ovalRectF = RectF()
    var arcPie = RectF()
    var pie = RectF()

    //arrow
    val arrowPath = Path()

    /*
        The init block will execute immediately after the primary constructor. Initializer blocks effectively become part of the primary constructor.

        The constructor is the secondary constructor. Delegation to the primary constructor happens as the first statement of a secondary constructor, so the code in all initializer blocks is executed before the secondary constructor body.
     */
    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingDemoInit() //Uncomment if you want to test DrawingDemo method
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawingDemo(canvas) // To test common drawing functions on canvas
        //canvasDemo(canvas)
    }

    fun drawingDemoInit() {
        fillRectF = RectF(
            width / 2 - width / 4f,
            height / 5 - 200f,
            width / 2 + width / 4f,
            height / 4 + 200f
        )

        outerRectF = RectF(
            width / 2 - width / 3f,
            height / 5 - 300f,
            width / 2 + width / 3f,
            height / 4 + 300f
        )

        ovalRectF = RectF(width / 2 - 150f, height / 2 - 150f, width / 2 + 150f, height / 2 + 150f)

        arcPie = RectF(
            width / 2f - width / 2 / 2f - 200f,
            height / 5 * 4 - 200f,
            width / 2f - width / 2 / 2f + 200f,
            height / 5 * 4 + 200f
        )

        pie = RectF(
            width / 2f - 200f,
            height / 5 * 4 - 200f,
            width / 2f + 200f,
            height / 5 * 4 + 200f
        )
    }

    fun drawingDemo(canvas: Canvas) {
        val string = "View size " + width + " x " + height
        canvas.drawPoint(width / 2f, 0f, pointPaint) // Indicate center

        canvas.drawText(
            string,
            width / 2f - outLinePaint.measureText(string) / 2,
            80f,
            outLinePaint
        ) // using measureText you can center text accordingly.

        fillPaint.color = Color.GREEN
        fillPaint.strokeWidth = 10f
        //Draw a line half of the width
        canvas.drawLine(
            width / 2f - width / 2 / 2,
            150f,
            width / 2f + width / 2 / 2,
            150f,
            fillPaint
        )

        fillPaint.color = Color.BLUE
        //inner rectangle
        canvas.drawRect(fillRectF, fillPaint)

        //outside rectangle
        canvas.drawRect(outerRectF, outLinePaint)

        fillPaint.color = Color.RED
        canvas.drawOval(ovalRectF, fillPaint)
        outLinePaint.color = Color.BLUE
        canvas.drawOval(ovalRectF, outLinePaint)

        //pacman
        fillPaint.color = Color.YELLOW
        outLinePaint.color = Color.BLACK

        //canvas.drawRect(arcPie, fillPaint)
        outLinePaint.strokeWidth = 10f

        var r = 0
        var g = 0
        var b = 0

        for (i in 0..360 step 90) {
            r = Random.nextInt(255 - 0 + 1) + 0
            g = Random.nextInt(255 - 0 + 1) + 0
            b = Random.nextInt(255 - 0 + 1) + 0 //a,b range. (b-a+1)+a
            fillPaint.color = Color.rgb(r, g, b)
            canvas.drawArc(arcPie, i.toFloat(), 90f, false, fillPaint)
            //canvas.drawArc(arcPie, i.toFloat(), 270f, false, outLinePaint)
        }


        outLinePaint.strokeWidth = 2f
        canvas.drawText("$r - $g - $b color selected", 50f, height - 100f, outLinePaint)

        //canvas.drawRect(pie, fillPaint)
        canvas.drawArc(pie, 320f, 80f, true, fillPaint)
        canvas.drawArc(pie, 320f, 80f, true, outLinePaint)
    }

    fun canvasDemo(canvas: Canvas) {
        val arrow_x = intArrayOf(0, 15,  5, 5, 15,  0, -15, -5,  -5,  -15)
        val arrow_y = intArrayOf(0,  40,  30,  120, + 160,  130,  160, 120,  30,  40)

        arrowPath.moveTo(arrow_x[0].toFloat(), arrow_y[0].toFloat())

        for(p in 1 until arrow_x.size){
            arrowPath.lineTo(arrow_x[p].toFloat(), arrow_y[p].toFloat())
        }
        arrowPath.close()

        canvas.save() //saving canvas will allow you to save the state so you can restore later with original coordinate system

        canvas.translate(width/2f, height/2f) // resets canvas 0,0 position to this coordinate system
        canvas.drawPath(arrowPath, fillPaint)

        // canvas.restore()
        canvas.rotate(45f) // clockwise rotation from 0,0 point
        canvas.drawPath(arrowPath, outLinePaint)

        fillPaint.color = Color.RED
        canvas.translate(0f, -200f)
        canvas.drawPath(arrowPath, fillPaint)

        fillPaint.color = Color.MAGENTA
        canvas.rotate(45f)
        canvas.translate(0f, -200f)
        canvas.drawPath(arrowPath, fillPaint)

        fillPaint.color = Color.BLUE
        //canvas.translate(0f, -100f) //flying up 100points
        canvas.rotate(90f) // 90 degree clockwise
        canvas.scale(1.5f, 1.5f) //bigger arrow by 1.5
        canvas.drawPath(arrowPath, outLinePaint)

        fillPaint.color = Color.CYAN
        canvas.translate(0f, -200f)
        canvas.drawPath(arrowPath, fillPaint)

        canvas.restore() // restoring will reset back to original, saving will prevent from changing coordinate system

        fillPaint.color = Color.RED
        canvas.rotate(315f)
        canvas.drawPath(arrowPath, fillPaint)
    }

}

/*
   drawArc angles visual
            270

     180           0

            90

 */

