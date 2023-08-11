package com.apprajapati.myanimations.util

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTimeOnly(dateTime: String) : String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val stringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX") //You can either use patter for DateTimeFormatter or use inbuilt constant for a particular datetime string format.
    val dateTimeParse = LocalDateTime.parse(dateTime, stringFormatter)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    return dateTimeParse.toLocalTime().format(timeFormatter)
}

/*
In case of worldTimeApi doesn't work, using TimeApi.io to fetch time which usus ISO_LOCAL_DATE_TIME formatter.
 */
fun getTimeOnlyTimeApi(dateTime: String) : String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val stringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX") //You can either use patter for DateTimeFormatter or use inbuilt constant for a particular datetime string format.
    val dateTimeParse = LocalDateTime.parse(dateTime, formatter)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    return dateTimeParse.toLocalTime().format(timeFormatter)
}

//Todo: fetch time with RegEx to learn.


fun rotateViewUsingObjectAnimator(animateView: View): ObjectAnimator {
    val animator = ObjectAnimator.ofFloat(animateView, "rotation", 0f, 360f)
    animator.duration = 1000
    animator.repeatCount = ObjectAnimator.INFINITE
    //animator.start()
    return animator
}

fun View.setGone(){
    this.visibility = View.GONE
}

fun pxToDp(context: Context, px: Float) : Int{
    return Math.round(px/context.resources.displayMetrics.density)
}