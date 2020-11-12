package com.kml.repositories

import com.kml.data.app.FileFactory
import com.kml.data.externalDbOperations.DbSendWork
import com.kml.data.models.WorkToAdd

class WorkTimerRepository(val fileFactory: FileFactory) {

    companion object {
        const val file = FileFactory.CURRENT_TIME_TXT
    }

    fun readFile():String
    {
        return fileFactory.readFromFile(file)
    }

    fun saveToFile(toSave: String)
    {
        fileFactory.saveStateToFile(toSave, file)
    }

    fun clearFileState()
    {
        fileFactory.clearFileState(file)
    }

    fun addWorkToDatabase(work: WorkToAdd): Boolean {
        val dbSendWork = DbSendWork(work)
        dbSendWork.start()
        return dbSendWork.result
    }
}