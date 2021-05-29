package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.extensions.logError
import com.kml.models.model.User
import com.kml.repositories.SplashScreenRepository

class SplashScreenViewModel(
    private val repository: SplashScreenRepository
) : ViewModel() {

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

    fun getSwitchDarkModeState(): Boolean {
        return repository.getSwitchDarkModeState()
    }
}