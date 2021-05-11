package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.repositories.MainRepository

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {


    fun saveSwitchDarkMode(state: Boolean) {
        repository.saveSwitchDarkMode(state.toString())
    }

    fun getSwitchDarkModeState(): Boolean {
        return repository.getSwitchDarkModeState()
    }

    fun clearLogData() {
        repository.clearLogData()
    }
}
