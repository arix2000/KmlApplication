package com.kml.repositories

import com.google.gson.Gson
import com.kml.data.networking.DbGetWorksHistory
import com.kml.data.networking.DbGetWorksHistory.Companion.GET_MEETINGS
import com.kml.data.networking.DbGetWorksHistory.Companion.GET_WORKS
import com.kml.extensions.async
import com.kml.models.Work
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single

class WorksHistoryRepository(private val fileFactory: FileFactory): BaseRepository() {
    var isFromFile = false

    fun getStringJsonWorks(shouldShowAll: Boolean): Single<String> {
        isFromFile = false
        return getWorks(shouldShowAll)
    }

    private fun getWorks(shouldShowAll: Boolean): Single<String> {
        val dbGetWorksHistory = DbGetWorksHistory(GET_WORKS, shouldShowAll)
        return Single.create<String> { it.onSuccess(dbGetWorksHistory.fetchResult()) }
                .async()
    }

    fun getStringJsonMeetings(shouldShowAll: Boolean): Single<String> {
        isFromFile = false
        return getMeetings(shouldShowAll)
    }

    private fun getMeetings(shouldShowAll: Boolean): Single<String> {
        val dbGetWorksHistory = DbGetWorksHistory(GET_MEETINGS, shouldShowAll)
        return Single.create<String>  { it.onSuccess(dbGetWorksHistory.fetchResult()) }
                .async()
    }

    fun readStringFrom(file: String): String {
        isFromFile = true
        return fileFactory.readFromFile(file)
    }

    fun saveStringTo(list: List<Work>, file: String) {
        fileFactory.saveStateToFile(Gson().toJson(list),file)
    }
}