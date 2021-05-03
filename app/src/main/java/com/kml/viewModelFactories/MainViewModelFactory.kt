package com.kml.viewModelFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.utilities.FileFactory
import com.kml.viewModels.MainViewModel

class MainViewModelFactory(val fileFactory: FileFactory): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(fileFactory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}