package com.kml.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kml.Constants.Keys.DATA_STORE_NAME
import com.kml.Constants.Strings.EMPTY_STRING

fun Context.hideSoftKeyboard(view: View) {
    val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, 0)
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

fun clearPools(vararg editTexts: EditText) {
    editTexts.forEach {
        it.setText(EMPTY_STRING)
    }
}