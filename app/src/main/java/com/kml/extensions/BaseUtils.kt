package com.kml.extensions

import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import com.kml.Constants.Date.NEW_DATE_OUTPUT_FORMAT
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.*

const val DEBUG_TAG = "DEBUG_TAG"
const val NETWORK_RESPONSE_TAG = "NETWORK_RESPONSE_TAG"

fun Any.log(message: Any = "", list: List<Any> = listOf()) {

    var logMessage = this.javaClass.simpleName + " ---> " + message.toString()

    if (list.isNotEmpty()) {
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

fun Any.logNetworkResponse(message: String) {
    val logMessage = this.javaClass.simpleName + " ---> " + message

    Log.i(NETWORK_RESPONSE_TAG, logMessage)
}

fun Calendar.getTodayDate(): String {
    val date =  get(Calendar.DAY_OF_MONTH).toString() + "." +
            (get(Calendar.MONTH) + 1) + "." +
            get(Calendar.YEAR)
    val validDate = SimpleDateFormat(NEW_DATE_OUTPUT_FORMAT, Locale.getDefault()).parse(date)
    return DateFormat.format(NEW_DATE_OUTPUT_FORMAT, validDate).toString()
}

fun Calendar.getCurrentMonth() = get(Calendar.MONTH)
fun Calendar.getCurrentYear() = get(Calendar.YEAR)

/**
 * Same as Editable.toSafeString
 *
 * @return safe string ready to send to database by for example php api
 * @see Editable.toSafeString
 */
fun String.toSafeString(): String {
    return this.replace("\\", "\\\\")
}

fun String.toIntOr(default: Int): Int {
    return try {
        this.toInt()
    } catch (e: NumberFormatException) {
        default
    }
}

/**
 * Format Editable to safe string for api requests by basically changing "\" char with "\\"
 *
 * @return safe string ready to send to database by for example php api
 */
fun Editable.toSafeString(): String {
    return this.toString().replace("\\", "\\\\")
}

fun <T> getDeferSingleFrom(async: () -> T): Single<T> {
    return Single.defer { Single.just(async()) }
}