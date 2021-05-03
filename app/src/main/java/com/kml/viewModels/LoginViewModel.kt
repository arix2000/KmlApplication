package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.app.KmlApp
import com.kml.data.utilities.FileFactory
import com.kml.extensions.logError
import com.kml.models.User
import com.kml.repositories.LoginRepository

class LoginViewModel(val fileFactory: FileFactory) : ViewModel() {
    private val repository = LoginRepository(fileFactory)

    fun checkLogin(login: String, password: String): Boolean {
        val result = repository.checkLoginForResult(login, password)

        return if (result.contains("true")) {
            getLoginId(result)
            true
        } else false
    }

    private fun getLoginId(logResult: String) {
        var result = logResult
        result = result.substring(result.length - 3)
        result = result.trim()
        KmlApp.loginId = result.toInt()
    }

    fun decideAboutSavingLogData(login: String, password: String, isChecked: Boolean) {
        repository.decideAboutSavingLogData(login, password, isChecked)
    }

    fun getLogData(): User {
        val result = repository.getLogDataIfExist()

        return if (result.isNotEmpty()) {
            val content = result.split(";".toRegex()).toTypedArray()
            createUserFrom(content)
        } else User.EMPTY
    }

    private fun createUserFrom(content: Array<String>): User {
        return try {
            User(content[0].toInt(), content[1], content[2])
        } catch (e: Exception) {
            logError(e)
            User.EMPTY
        }
    }

    fun getPreviousSwitchState(): Boolean {
        val fromFile = repository.getSwitchState()
        return if(fromFile.isNotEmpty())
            fromFile.toBoolean()
        else false
    }
}