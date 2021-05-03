package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.utilities.FileFactory
import com.kml.repositories.MainRepository

class MainViewModel(fileFactory: FileFactory): ViewModel() {
    private val repository = MainRepository(fileFactory)

    fun saveSwitchDarkMode(state: Boolean) {
        repository.saveSwitchDarkMode(state.toString())
    }

    fun getSwitchDarkModeState():Boolean {
        return repository.getSwitchDarkModeState()
    }

    fun clearLogData() {
        repository.clearLogData()
    }
}
