package com.apprajapati.myanimations.ui.fragments.MovingBallCustomView

import android.graphics.Color
import android.graphics.PointF

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