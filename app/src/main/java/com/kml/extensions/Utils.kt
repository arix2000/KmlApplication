package com.kml.extensions

import android.util.Log

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