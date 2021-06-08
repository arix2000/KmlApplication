package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.extensions.createWorkListFrom
import com.kml.models.dto.Work
import com.kml.repositories.WorksHistoryRepository
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single

class WorksHistoryViewModel(
    private val repository: WorksHistoryRepository
) : ViewModel() {

    var isSearchExpanded = false
    private var _cachedWorks: List<Work> = listOf()
    val cachedWorks get() = _cachedWorks

    fun fetchDataBy(type: String, shouldShowAll: Boolean): Single<List<Work>> {
        return if (type == WORKS_TAG)
            repository.getStringJsonWorks(shouldShowAll)
                    .doOnError { _cachedWorks = createWorkListFrom(getFromFileIfResultIsEmpty(type)) }
                    .doOnSuccess { repository.saveStringTo(it, getFilenameBy(type)) }
        else
            repository.getStringJsonMeetings(shouldShowAll)
                    .doOnError { _cachedWorks = createWorkListFrom(getFromFileIfResultIsEmpty(type)) }
                    .doOnSuccess { repository.saveStringTo(it, getFilenameBy(type)) }

    }

    private fun getFromFileIfResultIsEmpty(type: String): String {
        return repository.readStringFrom(getFilenameBy(type))
    }

    private fun getFilenameBy(type: String) =
        if (type == WORKS_TAG) FileFactory.HISTORY_KEEP_WORKS_TXT
        else FileFactory.HISTORY_KEEP_MEETINGS_TXT
}