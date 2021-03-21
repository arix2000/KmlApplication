package com.kml.data.utilities

import android.content.Context
import android.widget.Toast
import com.kml.Constants.Numbers.MINUTES_IN_ONE_HOUR
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Signal.EMPTY_POOLS
import com.kml.Constants.Signal.MAXIMUM_PERMITTED_HOURS
import com.kml.Constants.Signal.TOO_MANY_HOURS
import com.kml.Constants.Signal.TOO_MANY_MINUTES
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.models.WorkToAdd

class Validator(private val context: Context) {

    private fun isTooManyHours(hours: Int): Boolean = hours > MAXIMUM_PERMITTED_HOURS

    private fun isTooManyMinutes(minutes: Int): Boolean = minutes > MINUTES_IN_ONE_HOUR

    /**
     * validate is object has empty pools or have more than 60 minutes or have more than 14 hours
     * @return true when validation successful and false otherwise
     */
    fun validateWork(work: WorkToAdd): Boolean {
        val result = when {
            (isPoolsEmpty(work)) -> EMPTY_POOLS
            (isTooManyMinutes(work.minutes)) -> TOO_MANY_MINUTES
            (isTooManyHours(work.hours)) -> TOO_MANY_HOURS
            else -> VALIDATION_SUCCESSFUL
        }

        return if (result == VALIDATION_SUCCESSFUL) true else { makeToastBy(result); false }
    }


    private fun isPoolsEmpty(work: WorkToAdd): Boolean {
        return work.name.trim().isEmpty() || work.description.trim().isEmpty()
                || work.hours == TIME_HAS_NO_VALUE || work.minutes == TIME_HAS_NO_VALUE
    }

    private fun makeToastBy(resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }
}