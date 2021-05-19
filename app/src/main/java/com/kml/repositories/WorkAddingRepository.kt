package com.kml.repositories

import com.kml.data.networking.DbSendWork
import com.kml.models.WorkToAdd
import com.kml.utilities.FileFactory

class WorkAddingRepository(val fileFactory: FileFactory): BaseRepository() {
    fun addWorkToDatabase(work: WorkToAdd, onReceived: (Boolean)->Unit) {
        val dbSendWork = DbSendWork(work)
        dbSendWork.start()
        dbSendWork.setOnResultListener { onReceived(it.toBoolean()) }
    }
}