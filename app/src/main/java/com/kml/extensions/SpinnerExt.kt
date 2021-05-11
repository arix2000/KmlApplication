package com.kml.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.kml.R

fun <T> Spinner.createDefaultSpinnerAdapter(list: List<T>): ArrayAdapter<T> {
    return ArrayAdapter(context,
        R.layout.view_simple_spinner, list).apply {
        setDropDownViewResource(R.layout.view_simple_dropdown)
    }
}

fun Spinner.createDefaultSpinnerAdapter(arrayResId: Int): ArrayAdapter<CharSequence> {
    return ArrayAdapter.createFromResource(
        context,
        arrayResId,
        R.layout.view_simple_spinner
    ).apply {
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