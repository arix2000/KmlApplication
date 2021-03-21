package com.kml.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideSoftKeyboard(view: View) {
    val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, 0)
}