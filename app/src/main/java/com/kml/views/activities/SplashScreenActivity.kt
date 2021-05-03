package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.kml.R
import com.kml.data.app.KmlApp
import com.kml.data.utilities.FileFactory
import com.kml.models.User
import com.kml.viewModelFactories.SplashScreenViewModelFactory
import com.kml.viewModels.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {
    lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = SplashScreenViewModelFactory(FileFactory(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashScreenViewModel::class.java)
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