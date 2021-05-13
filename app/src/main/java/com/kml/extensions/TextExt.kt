package com.kml.extensions

import android.text.Editable


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

