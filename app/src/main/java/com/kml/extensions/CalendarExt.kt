package com.kml.extensions

import android.text.format.DateFormat
import com.kml.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getTodayDate(): String {
    val date =  get(Calendar.DAY_OF_MONTH).toString() + "." +
            (get(Calendar.MONTH) + 1) + "." +
            get(Calendar.YEAR)
    val validDate = SimpleDateFormat(Constants.Date.NEW_DATE_OUTPUT_FORMAT, Locale.getDefault()).parse(date)
    return DateFormat.format(Constants.Date.NEW_DATE_OUTPUT_FORMAT, validDate).toString()
}

fun Calendar.getCurrentMonth() = get(Calendar.MONTH)
fun Calendar.getCurrentYear() = get(Calendar.YEAR)
