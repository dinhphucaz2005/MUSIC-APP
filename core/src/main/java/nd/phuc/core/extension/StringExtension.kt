package nd.phuc.core.extension

import android.annotation.SuppressLint

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
fun Long?.toDurationString(): String {
    if (this == null || this == 0L) return "--:--"
    val hours = this / MILLIS_IN_HOUR
    val minutes = (this % MILLIS_IN_HOUR) / MILLIS_IN_MINUTE
    val seconds = (this % MILLIS_IN_MINUTE) / MILLIS_IN_SECOND

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}

@SuppressLint("DefaultLocale")
fun Int?.toDurationString(): String {
    if (this == null || this == 0) return ""
    val minutes = this / 60
    val seconds = this % 60

    return String.format("%02d:%02d", minutes, seconds)
}
