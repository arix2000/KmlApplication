package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kml.repositories.MainRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val isFromNotificationLiveData by lazy { MutableLiveData<Boolean>() }

    fun saveSwitchDarkMode(state: Boolean) {
        repository.saveSwitchDarkMode(state.toString())
    }

    fun getSwitchDarkModeState(): Boolean {
        return repository.getSwitchDarkModeState()
    }

    fun clearLogData() {
        repository.clearLogData()
    }

    fun getSavedIsFromNotification(): MutableLiveData<Boolean> {
        viewModelScope.launch {
            repository.getSavedIsFromNotification().collect {
                it?.let { isFromNotificationLiveData.value = it }
            }
        }
        return isFromNotificationLiveData
    }

    fun clearIsFromNotification() {
        repository.clearIsFromNotification()
    }
}
