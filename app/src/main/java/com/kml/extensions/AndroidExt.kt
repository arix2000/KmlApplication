package com.kml.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kml.Constants.Strings.EMPTY_STRING

fun Context.hideSoftKeyboard(view: View) {
    val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, 0)
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "workToAdd")

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun clearPools(vararg editTexts: EditText) {
    editTexts.forEach {
        it.setText(EMPTY_STRING)
    }
}