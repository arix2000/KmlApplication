package com.kml.repositories

import com.kml.data.externalDbOperations.DbGetWorksHistory
import com.kml.data.externalDbOperations.DbGetWorksHistory.Companion.GET_MEETINGS
import com.kml.data.externalDbOperations.DbGetWorksHistory.Companion.GET_WORKS
import com.kml.data.utilities.FileFactory

class WorksHistoryRepository(private val fileFactory: FileFactory) {
    var isFromFile = false

    fun getStringJsonWorks():String {
        var result: String
        result = resultWorks
        if (result.trim().isEmpty()) // no internet connection
        {
            result = fileFactory.readFromFile(FileFactory.HISTORY_KEEP_WORKS_TXT)
            isFromFile = true
        }
        fileFactory.saveStateToFile(result, FileFactory.HISTORY_KEEP_WORKS_TXT)

        return result
    }

    private val resultWorks: String
        get() {
            val dbGetWorksHistory = DbGetWorksHistory(GET_WORKS)
            dbGetWorksHistory.start()
            return dbGetWorksHistory.result
        }

    fun getStringJsonMeetings():String {
        var result: String
        result = resultMeetings
        if (result.trim().isEmpty()) // no internet connection
        {
            result = fileFactory.readFromFile(FileFactory.HISTORY_KEEP_MEETINGS_TXT)
            isFromFile = true
        }
        fileFactory.saveStateToFile(result, FileFactory.HISTORY_KEEP_MEETINGS_TXT)

        return result
    }

    private val resultMeetings: String
        get() {
            val dbGetWorksHistory = DbGetWorksHistory(GET_MEETINGS)
            dbGetWorksHistory.start()
            return dbGetWorksHistory.result
        }
}