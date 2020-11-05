package com.kml.viewModelFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.app.FileFactory
import com.kml.viewModels.LoginViewModel

class LoginViewModelFactory(val fileFactory: FileFactory): ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(fileFactory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}