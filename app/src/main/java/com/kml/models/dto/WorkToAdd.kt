package com.kml.models.dto

import com.kml.Constants.Numbers.SECONDS_IN_ONE_MINUTE

data class WorkToAdd(
    val name: String,
    val description: String,
    val hours: Int,
    val minutes: Int,
    val type: String = ""
) {
    fun getWorkTimeFloat() = (minutes / SECONDS_IN_ONE_MINUTE) + hours.toFloat()
    fun getWorkTimeReadable() = hours.toString() + "h " + minutes + "min"
}