package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.models.Volunteer
import com.kml.repositories.VolunteerRepository
import java.util.*

class VolunteersViewModel : ViewModel() {

    private val repository = VolunteerRepository()

    val volunteers: List<Volunteer> = createListFromJson(repository.readArrayFromDatabase())

    fun selectAllVolunteers() {
        for (volunteer in volunteers) {
            volunteer.isChecked = true
        }
    }

    fun deselectAllVolunteers() {
        for (volunteer in volunteers) {
            volunteer.isChecked = false
        }
    }

    fun filterArrayByName(typedText: String): List<Volunteer> {
        val filteredVolunteers: MutableList<Volunteer> = ArrayList()
        for (volunteer in volunteers) {
            if (volunteer.firstName.toLowerCase(Locale.ROOT).contains(typedText.toLowerCase(Locale.ROOT))) {
                filteredVolunteers.add(volunteer)
            }
        }
        return filteredVolunteers
    }

    private fun createListFromJson(jsonResult: String): List<Volunteer> {
        val gson = Gson()
        val type = object : TypeToken<List<Volunteer>>() {}.type
        return gson.fromJson(jsonResult, type) ?: emptyList()
    }

}






