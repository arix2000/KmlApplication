package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.utilities.FileFactory
import com.kml.extensions.createWorkListFrom
import com.kml.models.dto.Work
import com.kml.repositories.WorksHistoryRepository
import io.reactivex.rxjava3.core.Single

class WorksHistoryViewModel(
    private val repository: WorksHistoryRepository
) : ViewModel() {

    var isSearchExpanded = false

    fun fetchDataBy(type: String, shouldShowAll: Boolean): Single<List<Work>> {
        return if (type == WORKS_TAG)
            repository.getStringJsonWorks(shouldShowAll)
                    .map { createWorkListFrom(getFromFileIfResultIsEmpty(it,type)) }
                    .doOnSuccess { repository.saveStringTo(it, getFilenameBy(type)) }
        else
            repository.getStringJsonMeetings(shouldShowAll)
                    .map { createWorkListFrom(getFromFileIfResultIsEmpty(it,type)) }
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