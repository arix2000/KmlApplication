package com.kml

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    object Signal {
        const val EMPTY_POOLS = R.string.no_empty_fields
        const val TOO_MANY_MINUTES = R.string.too_many_minutes
        const val TOO_MANY_HOURS = R.string.too_many_hours
        const val VALIDATION_SUCCESSFUL = -1
        const val MAXIMUM_PERMITTED_HOURS = 14
        const val UNKNOWN_ID = -1
    }

    object Strings {
        const val EMPTY_STRING = ""
        const val TODAY = "Dzisiaj"
        const val SPACE = " "
    }

    object Tags {
        const val WORKS_TAG = "worksTag"
        const val MEETINGS_TAG = "meetingsTag"
        const val WORKS_HISTORY_TYPE = "WORKS_HISTORY_TAG"
        const val SHOULD_SHOW_BACK_BUTTON = "SHOULD_SHOW_BACK_BUTTON"
        const val GET_ALL_TAG = "getAllTag"
    }

    object Types {
        const val SNACK_BAR_TYPE = -1
        const val TOAST_TYPE = -2
    }

    object Numbers {
        const val TIME_HAS_NO_VALUE = -1
        const val MINUTES_IN_ONE_HOUR = 60
        const val INVALID_ID = -1
    }

    object Keys {
        val WORK_TO_ADD_KEY = stringPreferencesKey("WORK_TO_ADD_KEY")
    }
}