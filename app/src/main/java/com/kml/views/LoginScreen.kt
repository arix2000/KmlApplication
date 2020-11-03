package com.kml.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.databinding.ActivityLoginScreenBinding
import com.kml.viewModels.LoginViewModel
import com.kml.viewModels.LoginViewModelFactory
import java.lang.Boolean

class LoginScreen : AppCompatActivity() {
    private lateinit var cache: FileFactory
    lateinit var binding: ActivityLoginScreenBinding
    lateinit var viewModel: LoginViewModel

    companion object {
        @JvmField
        var isLog = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)

        cache = FileFactory(this)
        binding.login.setSelectAllOnFocus(true)
        binding.password.setSelectAllOnFocus(true)

        val viewModelFactory = LoginViewModelFactory(cache)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.logInButton.setOnClickListener {
            binding.loginScreenProgresBar.visibility = ProgressBar.VISIBLE
            Thread { logIn() }.start()
        }
    }

    private fun logIn() {
        val timeOnStart = SystemClock.elapsedRealtime()
        val intent = Intent(this, MainActivity::class.java)
        var toast = 0

        val login = binding.login.text.toString()
        val password = binding.password.text.toString()

        val result = viewModel.checkLogin(login, password)

        val timeOnEnd = SystemClock.elapsedRealtime()
        val elapsedTime = timeOnEnd - timeOnStart

        when {
            result -> {
                viewModel.decideAboutSavingLogData(login, password, binding.loginRememberMe.isChecked)
                startActivity(intent)
            }
            elapsedTime > 6000 -> toast = R.string.external_database_unavailable
            else -> toast = R.string.wrong_form_info
        }

        val handler = Handler(Looper.getMainLooper())
        val finalToast = toast
        handler.post {
            if (finalToast != 0) Toast.makeText(this@LoginScreen, finalToast, Toast.LENGTH_SHORT).show()
            binding.password.setText("")
            binding.loginScreenProgresBar.visibility = ProgressBar.GONE
        }
    }

    override fun onResume() {
        tryToAutoLogIn()
        isLog = true
        restoreSwitchState()
        super.onResume()
    }

    private fun tryToAutoLogIn() {

        val content = viewModel.getLogData()
        binding.login.setText(content.first)
        binding.password.setText(content.second)
    }

    private fun restoreSwitchState() {
        val fromFile = cache.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT)
        if (fromFile != null) {
            val previousSwitchState = Boolean.parseBoolean(fromFile)
            binding.loginRememberMe.isChecked = previousSwitchState
        }
    }
}