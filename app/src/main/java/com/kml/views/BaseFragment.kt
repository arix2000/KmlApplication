package com.kml.views

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.kml.extensions.hideBackButton
import com.kml.extensions.showBackButton

abstract class BaseFragment: Fragment() {

    var shouldShowBackButton = false

    /**
     * this var is used to set up progress bar for using hide/showProgressBar methods
     */
    private var baseProgressBar: View? = null

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

    open fun showProgressBar() {
        baseProgressBar?.visibility = View.VISIBLE
    }

    open fun hideProgressBar() {
        baseProgressBar?.visibility = View.GONE
    }

    fun attachProgressBar(progressBar: ProgressBar) {
        baseProgressBar = progressBar
    }
}