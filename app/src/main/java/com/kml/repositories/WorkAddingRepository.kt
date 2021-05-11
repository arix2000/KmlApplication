package com.kml.repositories

import com.kml.data.networking.DbSendWork
import com.kml.data.utilities.FileFactory
import com.kml.models.WorkToAdd

class WorkAddingRepository(val fileFactory: FileFactory) {
    fun addWorkToDatabase(work: WorkToAdd, onReceived: (Boolean)->Unit) {
        val dbSendWork = DbSendWork(work)
        dbSendWork.start()
        dbSendWork.setOnResultListener { onReceived(it.toBoolean()) }
    }
}