package com.apprajapati.myanimations.ui.fragments.MovingBallCustomView

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF

/*
Open class means they can be extended. By default all classes and methods are final, thus we cannot inherit them, if we want to inherit method or class, those should be mentioned with open keyword.
 */
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