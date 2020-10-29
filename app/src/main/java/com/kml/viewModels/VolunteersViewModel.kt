package com.kml.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.data.models.Volunteer
import com.kml.repositories.VolunteerRepository
import java.util.*
import kotlin.collections.ArrayList

class VolunteersViewModel : ViewModel() {

    private val repository = VolunteerRepository()

    val checkedVolunteers: ArrayList<Volunteer> = ArrayList()
    val volunteers: MutableLiveData<List<Volunteer>> by lazy {
        MutableLiveData<List<Volunteer>>(createListFromJson(repository.readArrayFromDatabase()))
    }

    fun addToCheckedVolunteers(volunteer: Volunteer) {
        if (!checkedVolunteers.contains(volunteer)) checkedVolunteers.add(volunteer)
    }

    fun removeFromCheckedVolunteers(volunteer: Volunteer) {
        for (i in checkedVolunteers.indices) {
            if (volunteer.id == checkedVolunteers[i].id) {
                checkedVolunteers.removeAt(i)
            }
        }
    }

    fun selectAllVolunteers() {
        for (volunteer in volunteers.value!!) {
            volunteer.isChecked = true
            addToCheckedVolunteers(volunteer)
        }
    }

    fun deselectAllVolunteers() {
        for (volunteer in volunteers.value!!) {
            volunteer.isChecked = false
        }
        checkedVolunteers.clear()
    }


    fun filterArrayByName(typedText: String): List<Volunteer> {
        val filteredVolunteers: MutableList<Volunteer> = java.util.ArrayList()
        for (volunteer in volunteers.value!!) {
            if (volunteer.firstName.toLowerCase(Locale.ROOT).contains(typedText.toLowerCase(Locale.ROOT))) {
                filteredVolunteers.add(volunteer)
            }
        }
        return filteredVolunteers
    }

    private fun createListFromJson(jsonResult: String): List<Volunteer> {
        val gson = Gson()
        val type = object : TypeToken<List<Volunteer>>() {}.type
        val volunteers: List<Volunteer> = gson.fromJson(jsonResult, type)
        Log.d("TESTING_TAG", "createListFromJson: "+jsonResult)
        return volunteers
    }
}