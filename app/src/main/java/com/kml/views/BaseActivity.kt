package com.kml.views

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    /**
     * return true if you want to do super.onBackPressed and false otherwise
     * **default: true**
     */
    var onBackPressed: () -> Boolean = { true }

    override fun onBackPressed() {
        if (onBackPressed.invoke())
            super.onBackPressed()
    }
}