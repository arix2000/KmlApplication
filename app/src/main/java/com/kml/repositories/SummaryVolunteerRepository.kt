package com.kml.repositories

import com.kml.data.externalDbOperations.DbAddingToChosen
import com.kml.models.WorkToAdd

class SummaryVolunteerRepository {
    private lateinit var addingToChosen: DbAddingToChosen

    fun sendWorkToDb(ids: String, volunteersNames: String, work: WorkToAdd): Boolean {
        addingToChosen = DbAddingToChosen(ids, volunteersNames, work)
        addingToChosen.start()

        return addingToChosen.result == "true"
    }
}