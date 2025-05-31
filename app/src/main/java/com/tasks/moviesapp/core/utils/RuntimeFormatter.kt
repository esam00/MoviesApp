package com.tasks.moviesapp.core.utils

fun formatMinutes(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60

    return buildString {
        if (hours > 0) {
            append("$hours hr")
            if (mins > 0) append(" ")
        }
        if (mins > 0 || hours == 0) {
            append("$mins min")
        }
    }
}