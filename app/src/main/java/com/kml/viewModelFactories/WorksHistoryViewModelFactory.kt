package com.kml.viewModelFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.app.FileFactory
import com.kml.viewModels.WorksHistoryViewModel

class WorksHistoryViewModelFactory(private val fileFactory: FileFactory): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(WorksHistoryViewModel::class.java)) {
            return WorksHistoryViewModel(fileFactory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}