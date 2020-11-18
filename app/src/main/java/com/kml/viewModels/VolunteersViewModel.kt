package com.kml.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.data.models.Volunteer
import com.kml.repositories.VolunteerRepository
import java.util.*

class VolunteersViewModel : ViewModel() {

    private val repository = VolunteerRepository()

    private val _volunteers: MutableLiveData<List<Volunteer>> by lazy {
        MutableLiveData<List<Volunteer>>(createListFromJson(repository.readArrayFromDatabase()))
    }

    val volunteers:LiveData<List<Volunteer>>
    get() {return _volunteers}

    fun selectAllVolunteers() {
        for (volunteer in _volunteers.value!!) {
            volunteer.isChecked = true
        }
    }

    fun deselectAllVolunteers() {
        for (volunteer in _volunteers.value!!) {
            volunteer.isChecked = false
        }
    }

    fun filterArrayByName(typedText: String): List<Volunteer> {
        val filteredVolunteers: MutableList<Volunteer> = ArrayList()
        for (volunteer in _volunteers.value!!) {
            if (volunteer.firstName.toLowerCase(Locale.ROOT).contains(typedText.toLowerCase(Locale.ROOT))) {
                filteredVolunteers.add(volunteer)
            }
        }
        return filteredVolunteers
    }

    private fun createListFromJson(jsonResult: String): List<Volunteer> {
        val gson = Gson()
        val type = object : TypeToken<List<Volunteer>>() {}.type
        Log.d("TAG_JSON_XD", "createListFromJson: $type <------> $jsonResult")
        return gson.fromJson(jsonResult, type) ?: emptyList()
    }
}