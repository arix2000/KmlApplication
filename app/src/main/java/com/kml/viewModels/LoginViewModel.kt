package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.app.FileFactory
import com.kml.data.app.KmlApp
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

    private fun getLoginId(result: String) {
        var result = result
        result = result.substring(result.length - 3)
        result = result.trim()
        KmlApp.loginId = result.toInt()
    }

    fun decideAboutSavingLogData(login: String, password: String, isChecked: Boolean) {
        repository.decideAboutSavingLogData(login, password, isChecked)
    }

    fun getLogData(): Pair<String, String> {
        val result = repository.getLogDataIfExist()

        return if (result.isNotEmpty()) {
            val content = result.split(";".toRegex()).toTypedArray()
            content[0] to content[1]
        } else Pair("", "")
    }

    fun getPreviousSwitchState(): Boolean {
        val fromFile = repository.getSwitchState()
        return if(fromFile.isNotEmpty())
            fromFile.toBoolean()
        else false
    }


}