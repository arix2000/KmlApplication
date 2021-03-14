package com.kml.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kml.R

fun AppCompatActivity.setFragment(fragment: Fragment, defaultAnim: Boolean = false) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
}

fun AppCompatActivity.setFragmentWithData(fragment: Fragment, data: Bundle, defaultAnim: Boolean = false) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment::class.java, data).commit()
}

fun Fragment.setFragment(fragment: Fragment, defaultAnim: Boolean = false) {
    parentFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
}

fun Fragment.setFragmentWithData(fragment: Fragment, data: Bundle, defaultAnim: Boolean = false) {
    parentFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment::class.java, data).commit()
}