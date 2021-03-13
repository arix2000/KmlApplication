package com.kml.repositories

import com.kml.data.externalDbOperations.DbAddingToChosen

class SummaryVolunteerRepository {
    private lateinit var addingToChosen: DbAddingToChosen

    fun sendWorkToDb(ids: String, volunteersNames: String, hours: Int, minutes: Int, workName: String, meetingDesc: String): Boolean {
        addingToChosen = DbAddingToChosen(ids, volunteersNames, workName, minutes, hours, meetingDesc)
        addingToChosen.start()
        return addingToChosen.result == "true"
    }
}