package com.apprajapati.myanimations.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTimeOnly(dateTime: String) : String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val stringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX") //You can either use patter for DateTimeFormatter or use inbuilt constant for that formatter.
    val dateTimeParse = LocalDateTime.parse(dateTime, stringFormatter)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    return dateTimeParse.toLocalTime().format(timeFormatter)
}

//Todo: fetch time with RegEx to learn.