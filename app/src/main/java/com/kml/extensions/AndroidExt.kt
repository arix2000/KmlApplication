package com.kml.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.R

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

fun <T> Spinner.createDefaultSpinnerAdapter(list: List<T>): ArrayAdapter<T> {
    return ArrayAdapter(context,
            R.layout.view_simple_spinner, list).apply {
        setDropDownViewResource(R.layout.view_simple_dropdown)
    }
}

/**
 * this is provides base parameters, if you want more, add more optionally
 */
fun Spinner.setOnItemSelectedListener(
        onNothingSelected: () -> Unit,
        onItemSelected: (view: View?, position: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(view, position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            onNothingSelected()
        }
    }
}