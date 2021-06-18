package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.KmlApp
import com.kml.extensions.logError
import com.kml.models.model.User
import com.kml.repositories.LoginRepository
import io.reactivex.rxjava3.core.Single

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    fun fetchLoginResult(login: String, password: String): Single<String> {
        return repository.fetchLoginResult(login, password)
            .doOnSuccess {
                if (it.contains("true"))
                    getLoginId(it)
            }
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
        return if (fromFile.isNotEmpty())
            fromFile.toBoolean()
        else false
    }
}