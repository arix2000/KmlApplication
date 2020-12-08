package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.data.models.Work
import com.kml.data.utilities.FileFactory
import com.kml.repositories.WorksHistoryRepository
import com.kml.views.fragments.WorksHistoryFragment

class WorksHistoryViewModel(fileFactory: FileFactory) : ViewModel() {

    private val repository = WorksHistoryRepository(fileFactory)
    private val works: List<Work>
        get() {
            return createListFromJson(repository.getStringJsonWorks())
        }

    private val meetings: List<Work>
        get() {
            return createListFromJson(repository.getStringJsonMeetings())
        }

    fun isFromFile(): Boolean = repository.isFromFile

    private fun createListFromJson(json: String): List<Work> {
        val gson = Gson()
        val type = object : TypeToken<List<Work>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getData(type: String): List<Work> {
        return if (type == WorksHistoryFragment.WORKS)
            works
        else meetings
    }

}