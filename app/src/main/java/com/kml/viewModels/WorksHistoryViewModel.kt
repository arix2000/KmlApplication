package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.data.utilities.FileFactory
import com.kml.models.Work
import com.kml.repositories.WorksHistoryRepository
import io.reactivex.rxjava3.core.Single

class WorksHistoryViewModel(fileFactory: FileFactory) : ViewModel() {

    private val repository = WorksHistoryRepository(fileFactory)

    var isSearchExpanded = false

    private fun createListFromJson(json: String): List<Work> {
        if (json.isBlank())
            return arrayListOf()

        val gson = Gson()
        val type = object : TypeToken<List<Work>>() {}.type
        return gson.fromJson(json, type)
    }

    fun fetchDataBy(type: String, shouldShowAll: Boolean): Single<List<Work>> {
        return if (type == WORKS_TAG)
            repository.getStringJsonWorks(shouldShowAll)
                    .map { createListFromJson(getFromFileIfResultIsEmpty(it,type)) }
                    .doOnSuccess { repository.saveStringTo(it, getFilenameBy(type)) }
        else
            repository.getStringJsonMeetings(shouldShowAll)
                    .map { createListFromJson(getFromFileIfResultIsEmpty(it,type)) }
                    .doOnSuccess { repository.saveStringTo(it, getFilenameBy(type)) }

    }

    private fun getFromFileIfResultIsEmpty(result: String, type: String): String {
        return if (result.isBlank()) {
            repository.readStringFrom(getFilenameBy(type))
        } else result
    }

    private fun getFilenameBy(type: String) =
        if (type == WORKS_TAG) FileFactory.HISTORY_KEEP_WORKS_TXT
        else FileFactory.HISTORY_KEEP_MEETINGS_TXT

    fun isFromFile(): Boolean = repository.isFromFile
}