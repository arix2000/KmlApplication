package com.kml

object Constants {

    object Signal {
        const val EMPTY_POOLS = R.string.no_empty_fields
        const val TOO_MANY_MINUTES = R.string.too_many_minutes
        const val TOO_MANY_HOURS = R.string.too_many_hours
        const val VALIDATION_SUCCESSFUL = -1
        const val MAXIMUM_PERMITTED_HOURS = 14
    }

    object Strings {
        const val EMPTY_STRING = ""
        const val TODAY = "Dzisiaj"
    }

    object Flags {
        const val WORKS = "WORKS"
        const val MEETINGS = "MEETINGS"
    }

    object Extras {
        const val WORKS_EXTRA = "WORKS_EXTRA"
        const val IS_FROM_FILE_EXTRA = "IS_FROM_FILE_EXTRA"
    }

    object Numbers {
        const val TIME_HAS_NO_VALUE = -1
        const val MINUTES_IN_ONE_HOUR = 60
    }
}