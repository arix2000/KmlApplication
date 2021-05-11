package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kml.KmlApp
import com.kml.R
import com.kml.models.User
import com.kml.viewModels.SplashScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {
    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        decideAboutDarkMode()
        setContentView(R.layout.activity_splash_screen)
        decideThenRoute()
    }

    private fun decideThenRoute() {
        val user = viewModel.getLogData()
        if (user.isEmpty()) {
            startActivity(Intent(this, LoginScreen::class.java))
        } else {
            setUserGlobalId(user)
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    private fun setUserGlobalId(user: User) {
        KmlApp.loginId = user.loginId
    }

    private fun decideAboutDarkMode() {
        val isDarkMode = viewModel.getSwitchDarkModeState()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}