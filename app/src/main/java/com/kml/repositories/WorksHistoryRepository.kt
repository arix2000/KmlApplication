package com.kml.repositories

import com.kml.data.utilities.FileFactory
import com.kml.data.externalDbOperations.DbGetWorksHistory

class WorksHistoryRepository(private val fileFactory: FileFactory) {
    var isFromFile = false

    fun getStringJsonWorks():String {
        var result: String
        result = resultFromExternalDb
        if (result.trim().isEmpty()) // no internet connection
        {
            result = fileFactory.readFromFile(FileFactory.HISTORY_KEEP_DATA_TXT)
            isFromFile = true
        }
        fileFactory.saveStateToFile(result, FileFactory.HISTORY_KEEP_DATA_TXT)

        return result
    }

    private val resultFromExternalDb: String
        get() {
            val dbGetWorksHistory = DbGetWorksHistory()
            dbGetWorksHistory.start()
            return dbGetWorksHistory.result
        }
}