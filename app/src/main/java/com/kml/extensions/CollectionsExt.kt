package com.kml.extensions

import com.kml.models.dto.Work

fun List<Work>.getWorksTimeTotal(): String {
    var hours = 0
    var minutes = 0
    this.forEach {
        val timeSeparated = it.executionTime.split(' ')
        hours += timeSeparated[0].removeSuffix("h").toInt()
        minutes += timeSeparated[1].removeSuffix("min").toInt()
    }
    val countedHours = (minutes/60) + hours
    val countedMinutes = minutes%60
    return "${countedHours}h ${countedMinutes}min"
}