package com.kml.repositories

import com.kml.data.externalDbOperations.DbAddingToChosen
import com.kml.extensions.asSafeString

class SummaryVolunteerRepository {
    private lateinit var addingToChosen: DbAddingToChosen

    fun sendWorkToDb(ids: String, volunteersNames: String, hours: Int, minutes: Int, workName: String, meetingDesc: String): Boolean {
        addingToChosen = DbAddingToChosen(ids, volunteersNames, workName.asSafeString(), minutes, hours, meetingDesc.asSafeString())
        addingToChosen.start()

        return addingToChosen.result == "true"
    }
}