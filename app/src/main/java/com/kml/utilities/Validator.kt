package com.kml.utilities

import androidx.fragment.app.FragmentActivity
import com.kml.Constants.Numbers.MINUTES_IN_ONE_HOUR
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Signal.EMPTY_POOLS
import com.kml.Constants.Signal.MAXIMUM_PERMITTED_HOURS
import com.kml.Constants.Signal.TOO_MANY_HOURS
import com.kml.Constants.Signal.TOO_MANY_MINUTES
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.Constants.Types.SNACK_BAR_TYPE
import com.kml.Constants.Types.TOAST_TYPE
import com.kml.R
import com.kml.extensions.logError
import com.kml.extensions.showSnackBar
import com.kml.extensions.showToast
import com.kml.models.WorkToAdd

class Validator(private val activity: FragmentActivity, var popupType: Int = SNACK_BAR_TYPE) {

    private fun isTooManyHours(hours: Int): Boolean = hours > MAXIMUM_PERMITTED_HOURS

    private fun isTooManyMinutes(minutes: Int): Boolean = minutes > MINUTES_IN_ONE_HOUR

    /**
     * Validate is any of pools are blank if yes, show popup about empty pools
     *
     * @return false if any of pools are blank or null, true otherwise
     */
    private fun checkPools(vararg pools: String?): Boolean {
        return if (pools.none { it.isNullOrBlank() })
            true
        else {
            showPopup(R.string.some_pools_are_empty)
            false
        }
    }

    /**
     * Validate is object has empty pools or have more than 60 minutes or have more than 14 hours
     * @return true when validation successful and false otherwise
     */
    fun validateWork(work: WorkToAdd): Boolean {
        val result = when {
            (isPoolsEmpty(work)) -> EMPTY_POOLS
            (isTooManyMinutes(work.minutes)) -> TOO_MANY_MINUTES
            (isTooManyHours(work.hours)) -> TOO_MANY_HOURS
            else -> VALIDATION_SUCCESSFUL
        }
        return if (result == VALIDATION_SUCCESSFUL) true else { showPopup(result); false }
    }


    private fun isPoolsEmpty(work: WorkToAdd): Boolean {
        return work.name.trim().isEmpty() || work.description.trim().isEmpty()
                || work.hours == TIME_HAS_NO_VALUE || work.minutes == TIME_HAS_NO_VALUE
    }

    private fun showPopup(resId: Int) {
        when(popupType) {
            TOAST_TYPE -> activity.showToast(resId)
            SNACK_BAR_TYPE -> activity.showSnackBar(resId)
            else -> logError(Throwable("Wrong popupType in Validator"))
        }
    }
}