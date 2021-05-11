package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.kml.R
import com.kml.databinding.ActivityLoginScreenBinding
import com.kml.extensions.showSnackBar
import com.kml.viewModels.LoginViewModel
import com.kml.views.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginScreen : BaseActivity() {
    lateinit var binding: ActivityLoginScreenBinding
    private val viewModel: LoginViewModel by viewModel()

    companion object {
        @JvmField
        var isLogNow = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
        binding.login.setSelectAllOnFocus(true)
        binding.password.setSelectAllOnFocus(true)
        binding.logInButton.setOnClickListener {
            binding.loginScreenProgressBar.visibility = ProgressBar.VISIBLE
            CoroutineScope(Dispatchers.Main).launch { logIn() }
        }
    }
//TODO fix threading please!!!
    private fun logIn() {
        val timeOnStart = SystemClock.elapsedRealtime()
        val intent = Intent(this, MainActivity::class.java)

        val login = binding.login.text.toString()
        val password = binding.password.text.toString()

        val result = viewModel.checkLogin(login, password)

        val timeOnEnd = SystemClock.elapsedRealtime()
        val elapsedTime = timeOnEnd - timeOnStart

        var toast = 0
        when {
            result -> {
                viewModel.decideAboutSavingLogData(login, password, binding.loginRememberMe.isChecked)
                startActivity(intent)
                finish()
            }
            elapsedTime > 6000 -> toast = R.string.external_database_unavailable
            else -> toast = R.string.wrong_form_info
        }

        val finalToast = toast
        if (finalToast != 0) showSnackBar(finalToast)
        binding.password.setText("")
        binding.loginScreenProgressBar.visibility = ProgressBar.GONE
    }

    override fun onResume() {
        tryToAutoLogIn()
        isLogNow = true
        restoreSwitchState()
        super.onResume()
    }

    private fun tryToAutoLogIn() {
        val content = viewModel.getLogData()
        binding.login.setText(content.firstName)
        binding.password.setText(content.lastName)
    }

    private fun restoreSwitchState() {
        binding.loginRememberMe.isChecked = viewModel.getPreviousSwitchState()
    }
}