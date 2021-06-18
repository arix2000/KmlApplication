package com.kml.extensions

import android.view.View
import androidx.lifecycle.LifecycleOwner

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun LifecycleOwner.visibleAll(vararg views: View) {
    views.forEach { it.visible() }
}

fun LifecycleOwner.goneAll(vararg views: View) {
    views.forEach { it.gone() }
}

fun LifecycleOwner.invisibleAll(vararg views: View) {
    views.forEach { it.invisible() }
}