package com.kml.extensions

import android.util.Log
import java.util.*

const val DEBUG_TAG = "DEBUG_TAG"

fun Any.log(message: String = "", list: List<Any> = listOf()) {
    var logMessage = this.javaClass.simpleName + " ---> " + message

    if(list.isNotEmpty()) {
        logMessage = "\n"
        for (item in list)
            logMessage = logMessage.plus("$item \n")
    }

    Log.d(DEBUG_TAG, logMessage)
}

fun Any.logError(message: Throwable) {
    val logMessage = this.javaClass.simpleName + " ---> " + message.message

    Log.e(DEBUG_TAG, logMessage)
}

fun Calendar.getTodayDate(): String {
    apply {
        return get(Calendar.DAY_OF_MONTH).toString() + "." +
                (get(Calendar.MONTH)+1) + "." +
                get(Calendar.YEAR) //TODO Export this formatting to FormatEngine. In MyDatePickerDialog we have similar operation
    }
}

/**
 * Format string to safe for api requests by basically changing "\" char with "\\"
 *
 * @return safe string ready to send to database by for example php api
 */

fun String.asSafeString(): String {
    return this.replace("\\","\\\\")

}