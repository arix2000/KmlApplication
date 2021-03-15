package com.kml.views

import androidx.fragment.app.Fragment
import com.kml.extensions.hideBackButton
import com.kml.extensions.showBackButton

abstract class BaseFragment: Fragment() {

    var shouldShowBackButton = false

    override fun onResume() {
        super.onResume()
        if (shouldShowBackButton)
            showBackButton()
    }

    override fun onPause() {
        super.onPause()
        if (shouldShowBackButton)
            hideBackButton()
    }
}