package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.repositories.SummaryVolunteerRepository

class SummaryVolunteerViewModel : ViewModel() {

    private val repository = SummaryVolunteerRepository()
    lateinit var times: List<TimeToVolunteers>

    fun createReadableFromVolunteers(): String {
        var chosenVolunteersMerged = ""
        for(time in times) {
            chosenVolunteersMerged += "\n"+time.hours+"h "+time.minutes+"min: \n"
            chosenVolunteersMerged += createStringFromVolunteers(time.volunteers)
            chosenVolunteersMerged.substring(0, chosenVolunteersMerged.length - 2) + "."
        }

        return chosenVolunteersMerged
    }

    private fun createStringFromVolunteers(chosenVolunteers: List<Volunteer>): String {
        val stringBuilder = StringBuilder()
        for (volunteer in chosenVolunteers) {
            val oneVolunteer = volunteer.firstName + " " + volunteer.lastName + ", "
            stringBuilder.append(oneVolunteer)
        }
        return stringBuilder.toString()
    }

    fun addWorkToDatabase(hours: Int, minutes: Int, workName: String): Boolean {
        var result = false
        for (time in times) {
            val ids = getIdsStringFromVolunteers(time.volunteers)
            val volunteersNames = createStringFromVolunteers(time.volunteers)
            result = repository.sendWorkToDb(ids, volunteersNames, hours, minutes, workName)
        }
        return result
    }

    private fun getIdsStringFromVolunteers(chosenVolunteers: List<Volunteer>): String {
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