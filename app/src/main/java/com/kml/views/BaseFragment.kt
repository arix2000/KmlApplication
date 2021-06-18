package com.kml.views

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.kml.extensions.hideBackButton
import com.kml.extensions.showBackButton

abstract class BaseFragment : Fragment() {

    private var activityTitle = ""
    var shouldShowBackButton = false

    /**
     * this var is used to set up progress bar for using hide/showProgressBar methods
     */
    private var baseProgressBar: View? = null

    override fun onResume() {
        super.onResume()
        if (shouldShowBackButton)
            showBackButton()
        else
            hideBackButton()
    }

    override fun onStop() {
        super.onStop()
        if (activityTitle.isNotBlank())
            activity?.title = activityTitle
    }

    open fun showProgressBar() {
        baseProgressBar?.visibility = View.VISIBLE
    }

    open fun hideProgressBar() {
        baseProgressBar?.visibility = View.GONE
    }

    /**
     * Attach your progress bar and then hide/show it by showProgressBar() and hideProgressBar()
     */
    fun attachProgressBar(progressBar: ProgressBar) {
        baseProgressBar = progressBar
    }

    /**
     * @param onBackPressed should return true if you want to do super.onBackPressed and false otherwise
     */
    fun setOnBackPressedListener(onBackPressed: () -> Boolean) {
        (activity as? BaseActivity)?.onBackPressed = onBackPressed
    }

    fun setTitle(title: String) {
        activityTitle = activity?.title.toString()
        activity?.title = title
    }
}