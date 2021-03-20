package com.kml.data.utilities

import android.content.Context
import com.kml.Constants.Numbers.MINUTES_IN_ONE_HOUR
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Signal.EMPTY_POOLS
import com.kml.Constants.Signal.MAXIMUM_PERMITTED_HOURS
import com.kml.Constants.Signal.TOO_MANY_HOURS
import com.kml.Constants.Signal.TOO_MANY_MINUTES
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.models.WorkToAdd

class Validator(private val context: Context) {

    fun isTooManyHours(hours: Int): Boolean = hours <= MAXIMUM_PERMITTED_HOURS

    fun isTooManyMinutes(minutes: Int): Boolean = minutes < MINUTES_IN_ONE_HOUR

    fun validateWorkInstant(work: WorkToAdd): Int {
        return when {
            (isPoolsEmpty(work)) -> EMPTY_POOLS
            (work.minutes > 60) -> TOO_MANY_MINUTES
            (work.hours>MAXIMUM_PERMITTED_HOURS) -> TOO_MANY_HOURS
            else -> VALIDATION_SUCCESSFUL
        }
    }

    private fun isPoolsEmpty(work: WorkToAdd): Boolean {
        return work.name.trim().isEmpty() || work.description.trim().isEmpty()
                || work.hours == TIME_HAS_NO_VALUE || work.minutes == TIME_HAS_NO_VALUE
    }
}