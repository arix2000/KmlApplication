package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.data.app.FileFactory
import com.kml.data.models.Work
import com.kml.repositories.WorksHistoryRepository

class WorksHistoryViewModel(fileFactory: FileFactory): ViewModel() {

    private val repository = WorksHistoryRepository(fileFactory)
    val works: MutableLiveData<List<Work>> by lazy {
        MutableLiveData<List<Work>>(createListFromJson(repository.getStringJsonWorks()))
    }

    fun isFromFile():Boolean = repository.isFromFile

    private fun createListFromJson(jsonWorks: String): List<Work> {
        val gson = Gson()
        val type = object : TypeToken<List<Work>>() {}.type
        return gson.fromJson(jsonWorks, type)
    }

}