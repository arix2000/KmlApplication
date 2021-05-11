package com.kml.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.kml.R
import com.kml.views.activities.MainActivity

fun AppCompatActivity.setFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    withAnim: Boolean = false
) {
    supportFragmentManager.commit {
        if (addToBackStack) addToBackStack(fragment.tag)
        if (withAnim) setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        replace(R.id.fragment_container,fragment)
    }
}

fun AppCompatActivity.setFragmentWithData(
    fragment: Fragment,
    data: Bundle,
    addToBackStack: Boolean = false,
    withAnim: Boolean = false
) {
    supportFragmentManager.commit {
        if (addToBackStack) addToBackStack(fragment.tag)
        if (withAnim) setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        replace(R.id.fragment_container,fragment::class.java, data)
    }
}

fun Fragment.setFragment(fragment: Fragment) {
    parentFragmentManager.commit {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        replace(R.id.fragment_container,fragment)
        addToBackStack(fragment.tag)
    }
}

fun Fragment.setFragmentWithData(fragment: Fragment, data: Bundle) {
    parentFragmentManager.commit {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        replace(R.id.fragment_container,fragment::class.java, data)
        addToBackStack(fragment.tag)
    }
}

fun Fragment.showBackButton() {
    (activity as? MainActivity)?.apply {
         if (!isBackButtonVisible)
             showBackButton()
    }
}

fun Fragment.hideBackButton() {
    (activity as? MainActivity)?.apply {
        if (isBackButtonVisible)
            hideBackButton()
    }
}