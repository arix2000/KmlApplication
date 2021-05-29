package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kml.Constants.Keys.IS_FROM_NOTIFICATION_BUNDLE_KEY
import com.kml.KmlApp
import com.kml.R
import com.kml.models.model.User
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

    private fun decideAboutDarkMode() {
        val isDarkMode = viewModel.getSwitchDarkModeState()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun decideThenRoute() {
        val user = viewModel.getLogData()
        if (user.isEmpty()) {
            startActivity(Intent(this, LoginScreen::class.java))
        } else {
            setUserGlobalId(user)
            val isFromNotification = intent.getBooleanExtra(IS_FROM_NOTIFICATION_BUNDLE_KEY, false)
            val intent = Intent(this, MainActivity::class.java)
                .putExtra(IS_FROM_NOTIFICATION_BUNDLE_KEY,isFromNotification)
            startActivity(intent)
        }
        finish()
    }

    private fun setUserGlobalId(user: User) {
        KmlApp.loginId = user.loginId
    }
}