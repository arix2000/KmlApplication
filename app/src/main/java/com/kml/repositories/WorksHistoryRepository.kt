package com.kml.repositories

import com.kml.data.externalDbOperations.DbGetWorksHistory
import com.kml.data.externalDbOperations.DbGetWorksHistory.Companion.GET_MEETINGS
import com.kml.data.externalDbOperations.DbGetWorksHistory.Companion.GET_WORKS
import com.kml.data.utilities.FileFactory
import com.kml.extensions.getDeferSingleFrom
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WorksHistoryRepository(private val fileFactory: FileFactory) {
    var isFromFile = false

    fun getStringJsonWorks(shouldShowAll: Boolean): Single<String> {
        isFromFile = false
        return getWorks(shouldShowAll)
    }

    private fun getWorks(shouldShowAll: Boolean): Single<String> {
        val dbGetWorksHistory = DbGetWorksHistory(GET_WORKS, shouldShowAll)
        return getDeferSingleFrom { dbGetWorksHistory.fetchResult() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStringJsonMeetings(shouldShowAll: Boolean): Single<String> {
        isFromFile = false
        return getMeetings(shouldShowAll)
    }

    private fun getMeetings(shouldShowAll: Boolean): Single<String> {
        val dbGetWorksHistory = DbGetWorksHistory(GET_MEETINGS, shouldShowAll)
        return getDeferSingleFrom { dbGetWorksHistory.fetchResult() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun readStringFrom(file: String): String {
        isFromFile = true
        return fileFactory.readFromFile(file)
    }

    fun saveStringTo(file: String): String {
        return fileFactory.readFromFile(file)
    }
}