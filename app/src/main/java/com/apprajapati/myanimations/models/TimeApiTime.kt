package com.apprajapati.myanimations.models

data class TimeApiTime(
    val date: String,
    val dateTime: String,
    val day: Int,
    val dayOfWeek: String,
    val dstActive: Boolean,
    val hour: Int,
    val milliSeconds: Int,
    val minute: Int,
    val month: Int,
    val seconds: Int,
    val time: String,
    val timeZone: String,
    val year: Int
)