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
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.app.KmlApp
import com.kml.data.externalDbOperations.DbLogin
import com.kml.databinding.ActivityLoginScreenBinding
import java.lang.Boolean

class LoginScreen : AppCompatActivity() {
    private lateinit var cache: FileFactory
    lateinit var binding: ActivityLoginScreenBinding

    companion object {
        @JvmField
        var isLog = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_screen)

        cache = FileFactory(this)
        binding.login.setSelectAllOnFocus(true)
        binding.password.setSelectAllOnFocus(true)
        binding.logInButton.setOnClickListener {
            binding.loginScreenProgresBar.visibility = ProgressBar.VISIBLE
            Thread { logIn() }.start()
        }
    }

    private fun logIn() {
        val timeOnStart = SystemClock.elapsedRealtime()
        val intent = Intent(this, MainActivity::class.java)
        var toast = 0
        val result: String
        val login = binding.login.text.toString()
        val password = binding.password.text.toString()

        val dbLogin = DbLogin(login, password)
        dbLogin.start()
        result = dbLogin.result

        val timeOnEnd = SystemClock.elapsedRealtime()
        val elapsedTime = timeOnEnd - timeOnStart

        if (result.contains("true")) {
            decideAboutSavingLogData(login, password)
            getLoginId(result)
            startActivity(intent)
        } else if (elapsedTime > 6000) {
            toast = R.string.external_database_unavailable
        } else {
            toast = R.string.wrong_form_info
        }

        val handler = Handler(Looper.getMainLooper())
        val finalToast = toast
        handler.post {
            if (finalToast != 0) Toast.makeText(this@LoginScreen, finalToast, Toast.LENGTH_SHORT).show()
            binding.password.setText("")
            binding.loginScreenProgresBar.visibility = ProgressBar.GONE
        }
    }

    private fun decideAboutSavingLogData(login: String, password: String) {
        if (binding.loginRememberMe.isChecked) {
            cache.saveStateToFile("$login;$password", FileFactory.DATA_TXT)
        } else {
            cache.saveStateToFile("", FileFactory.DATA_TXT) //clear File
        }
        cache.saveStateToFile(binding.loginRememberMe.isChecked.toString(), FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT)
    }

    private fun getLoginId(result: String) {
        var result = result
        result = result.substring(result.length - 3)
        result = result.trim()
        KmlApp.loginId = result.toInt()
    }

    override fun onResume() {
        tryToAutoLogIn()
        isLog = true
        restoreSwitchState()
        super.onResume()
    }

    private fun tryToAutoLogIn() {
        if (cache.readFromFile(FileFactory.DATA_TXT) != null) {
            if (cache.readFromFile(FileFactory.DATA_TXT).contains(";")) {
                val content = cache.readFromFile(FileFactory.DATA_TXT).split(";".toRegex()).toTypedArray()
                binding.login.setText(content[0])
                binding.password.setText(content[1])
            }
        }
    }

    private fun restoreSwitchState() {
        val fromFile = cache.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT)
        if (fromFile != null) {
            val previousSwitchState = Boolean.parseBoolean(fromFile)
            binding.loginRememberMe.isChecked = previousSwitchState
        }
    }
}