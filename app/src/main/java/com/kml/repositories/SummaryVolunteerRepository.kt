package com.kml.repositories

import com.kml.data.externalDbOperations.DbAddingToChosen

class SummaryVolunteerRepository {
    private lateinit var addingToChosen: DbAddingToChosen

    fun sendWorkToDb(ids: String, volunteersNames: String, hours: Int, minutes: Int, workName: String, date: String): Boolean {
        addingToChosen = DbAddingToChosen(ids, volunteersNames, workName, minutes, hours, date)
        addingToChosen.start()
        return addingToChosen.result == "true"
    }


}