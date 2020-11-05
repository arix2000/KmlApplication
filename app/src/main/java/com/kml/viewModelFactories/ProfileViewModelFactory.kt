package com.kml.viewModelFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.app.FileFactory
import com.kml.viewModels.ProfileViewModel

class ProfileViewModelFactory(val fileFactory: FileFactory): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(fileFactory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}