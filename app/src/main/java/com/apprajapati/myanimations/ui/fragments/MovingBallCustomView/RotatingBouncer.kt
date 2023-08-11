package com.apprajapati.myanimations.ui.fragments.MovingBallCustomView

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
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