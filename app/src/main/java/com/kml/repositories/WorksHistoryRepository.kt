package com.kml.repositories

import com.google.gson.Gson
import com.kml.KmlApp
import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.models.dto.Work
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single

class WorksHistoryRepository(
    private val fileFactory: FileFactory,
    private val restApi: RestApi
    ): BaseRepository() {
    var isFromFile = false

    fun getStringJsonWorks(shouldShowAll: Boolean): Single<List<Work>> {
        isFromFile = false
        return getWorks(shouldShowAll).async()
    }

    private fun getWorks(shouldShowAll: Boolean): Single<List<Work>> {
        return if (shouldShowAll)
            restApi.fetchWorkHistory(WORK_COMMAND_FOR_ALL, WORK_COMMAND_FOR_ALL)
        else
            restApi.fetchWorkHistory(KmlApp.firstName, KmlApp.lastName)
    }

    fun getStringJsonMeetings(shouldShowAll: Boolean): Single<List<Work>> {
        isFromFile = false
        return getMeetings(shouldShowAll).async()
    }

    private fun getMeetings(shouldShowAll: Boolean): Single<List<Work>> {
        return if (shouldShowAll)
            restApi.fetchMeetingsHistory(MEETINGS_COMMAND_FOR_ALL, MEETINGS_COMMAND_FOR_ALL)
        else
            restApi.fetchMeetingsHistory(KmlApp.firstName, KmlApp.lastName)
    }

    fun readStringFrom(file: String): String {
        isFromFile = true
        return fileFactory.readFromFile(file)
    }

    fun saveStringTo(list: List<Work>, file: String) {
        fileFactory.saveStateToFile(Gson().toJson(list),file)
    }

    companion object {
        const val WORK_COMMAND_FOR_ALL = "%%"
        const val MEETINGS_COMMAND_FOR_ALL = ""
    }
}