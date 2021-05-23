package com.kml

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    object Signal {
        const val EMPTY_POOLS = R.string.no_empty_fields
        const val TOO_MANY_MINUTES = R.string.too_many_minutes
        const val TOO_MANY_HOURS = R.string.too_many_hours
        const val VALIDATION_SUCCESSFUL = -1
        const val MAXIMUM_PERMITTED_HOURS = 14
        const val UNKNOWN_ID = -1
        const val EMPTY_ID = -1
    }

    object Strings {
        const val EMPTY_STRING = ""
        const val TODAY = "Dzisiaj"
        const val SPACE = " "
        const val SPACE_CHAR = ' '
        const val NO_TYPE = "Brak"
    }

    object Tags {
        const val WORKS_TAG = "worksTag"
        const val MEETINGS_TAG = "meetingsTag"
        const val WORKS_HISTORY_TYPE = "WORKS_HISTORY_TAG"
        const val SHOULD_SHOW_BACK_BUTTON = "SHOULD_SHOW_BACK_BUTTON"
        const val GET_ALL_TAG = "getAllTag"
        const val REMAINDER_WORKER_UNIQUE_NAME = "RemainderWorker"
    }

    object Date {
        const val OLD_DATE_INPUT_FORMAT = "d.M.yyyy"
        const val NEW_DATE_OUTPUT_FORMAT = "dd.MM.yyyy"
        const val CREATION_DATE_FORMAT = "dd-MM-yyyy"
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
        const val DATA_STORE_NAME = "mainPreferencesDataStore"
        val WORK_TO_ADD_KEY = stringPreferencesKey("WORK_TO_ADD_KEY")
        val IS_FROM_NOTIFICATION_KEY = booleanPreferencesKey("IS_FROM_NOTIFICATION_KEY")
        const val IS_FROM_NOTIFICATION_BUNDLE_KEY = "IS_FROM_NOTIFICATION__BUNDLE_KEY"
    }
}