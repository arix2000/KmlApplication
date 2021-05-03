package com.kml.viewModelFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.utilities.FileFactory
import com.kml.viewModels.SplashScreenViewModel

class SplashScreenViewModelFactory(val fileFactory: FileFactory): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(fileFactory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}