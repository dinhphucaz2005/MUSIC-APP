package com.example.musicapp.extension

import android.annotation.SuppressLint

fun String.getFileNameExtension(): String {
    val index = this.lastIndexOf('.')
    return if (index == -1) "" else this.substring(index + 1)
}

fun String.getFileNameWithoutExtension(): String {
    val index = this.lastIndexOf('.')
    return if (index == -1) this else this.substring(0, index)
}


/**
 * Converts time from milliseconds to a formatted string in hh:mm:ss or mm:ss.
 *
 * @receiver Time in milliseconds (Long).
 * @return Formatted time string.
 */
const val MILLIS_IN_SECOND = 1000
const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND
const val MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE

@SuppressLint("DefaultLocale")
fun Long?.toDuration(): String {
    if (this == null) return "00:00"
    val hours = this / MILLIS_IN_HOUR
    val minutes = (this % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE
    val seconds = (this % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}