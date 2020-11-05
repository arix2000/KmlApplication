package com.kml.views

import android.content.Intent
import android.os.Bundle
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
import com.kml.viewModelFactories.LoginViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginScreen : AppCompatActivity() {
    private lateinit var cache: FileFactory
    lateinit var binding: ActivityLoginScreenBinding
    lateinit var viewModel: LoginViewModel

    companion object {
        @JvmField
        var isLogNow = false
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
            binding.loginScreenProgressBar.visibility = ProgressBar.VISIBLE

            CoroutineScope(Dispatchers.Main).launch { logIn() }

        }
    }

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
            }
            elapsedTime > 6000 -> toast = R.string.external_database_unavailable
            else -> toast = R.string.wrong_form_info
        }

        val finalToast = toast
        if (finalToast != 0) Toast.makeText(this@LoginScreen, finalToast, Toast.LENGTH_SHORT).show()
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
        binding.login.setText(content.first)
        binding.password.setText(content.second)
    }

    private fun restoreSwitchState() {
        binding.loginRememberMe.isChecked = viewModel.getPreviousSwitchState()
    }
}