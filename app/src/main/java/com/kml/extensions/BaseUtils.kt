package com.kml.extensions

import android.util.Log
import io.reactivex.rxjava3.core.Single
import java.util.*

const val DEBUG_TAG = "DEBUG_TAG"
const val NETWORK_RESPONSE_TAG = "NETWORK_RESPONSE_TAG"

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

fun Any.logNetworkResponse(message: String) {
    val logMessage = this.javaClass.simpleName + " ---> " + message

    Log.i(NETWORK_RESPONSE_TAG, logMessage)
}

fun Calendar.getTodayDate(): String {
    apply {
        return get(Calendar.DAY_OF_MONTH).toString() + "." +
                (get(Calendar.MONTH)+1) + "." +
                get(Calendar.YEAR)
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

fun String.toIntOr(default: Int): Int {
    return try {
        this.toInt()
    } catch (e: NumberFormatException) {
        default
    }
}

fun <T> getDeferSingleFrom(async: ()-> T): Single<T> {
    return Single.defer { Single.just(async()) }
}