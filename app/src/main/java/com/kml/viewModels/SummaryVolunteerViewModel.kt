package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.models.Volunteer
import com.kml.repositories.SummaryVolunteerRepository

class SummaryVolunteerViewModel : ViewModel() {

    private val repository = SummaryVolunteerRepository()
    lateinit var chosenVolunteers: List<Volunteer>

    fun createReadableFromVolunteers(): String {
        val chosenVolunteersMerged = createStringFromVolunteers()
        return chosenVolunteersMerged.substring(0, chosenVolunteersMerged.length - 2) + "."
    }

    private fun createStringFromVolunteers(): String {
        val stringBuilder = StringBuilder()
        for (volunteer in chosenVolunteers) {
            val oneVolunteer = volunteer.firstName + " " + volunteer.lastName + ", "
            stringBuilder.append(oneVolunteer)
        }
        return stringBuilder.toString()
    }

    fun addWorkToDatabase(hours: Int, minutes: Int, workName: String): Boolean {
        val ids = getIdsStringFromVolunteers()
        val volunteersNames = createStringFromVolunteers()
        return repository.sendWorkToDb(ids, volunteersNames, hours, minutes, workName)

    }

    private fun getIdsStringFromVolunteers(): String {
        val ids = StringBuilder()
        var cache: String
        for (volunteer in chosenVolunteers) {
            if (!ids.toString().contains(volunteer.id.toString())) {
                cache = volunteer.id.toString() + ","
                ids.append(cache)
            }
        }
        ids.replace(ids.length - 1, ids.length, "")
        return ids.toString()
    }
}