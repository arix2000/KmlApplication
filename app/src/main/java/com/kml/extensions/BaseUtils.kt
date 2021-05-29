package com.kml.extensions

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.models.dto.Work

const val DEBUG_TAG = "DEBUG_TAG"
const val NETWORK_RESPONSE_TAG = "NETWORK_RESPONSE_TAG"

fun Any.log(message: Any? = "", list: List<Any> = listOf()) {

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

fun createWorkListFrom(json: String): List<Work> {
    if (json.isBlank())
        return arrayListOf()

    val type = object : TypeToken<List<Work>>() {}.type
    return Gson().fromJson(json, type)
}