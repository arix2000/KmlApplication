package com.kml.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kml.R
import com.kml.views.activities.MainActivity

fun AppCompatActivity.setFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).apply {
        if (addToBackStack) addToBackStack(fragment.tag)
        commit()
    }
}

fun AppCompatActivity.setFragmentWithData(fragment: Fragment, data: Bundle) {
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment::class.java, data).commit()
}

fun Fragment.setFragment(fragment: Fragment) {
    parentFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container,fragment).addToBackStack(fragment.tag).commit()
}

fun Fragment.setFragmentWithData(fragment: Fragment, data: Bundle) {
    parentFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragment_container,fragment::class.java, data).addToBackStack(fragment.tag).commit()
}

fun Fragment.showBackButton() {
    (activity as? MainActivity)?.showBackButton()
}

fun Fragment.hideBackButton() {
    (activity as? MainActivity)?.hideBackButton()
}