package com.kml.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.models.Volunteer
import com.kml.repositories.VolunteerRepository
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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

    private fun createListFromJson(result: String): List<Volunteer> {
        var volunteers: List<Volunteer> = ArrayList()
        try {
            val jsonArray = JSONArray(result)
            volunteers = fillArrayFromJson(jsonArray)
        } catch (e: JSONException) {
            Log.d("RESULT_FROM_JSON", "onException: " + e.message)
        }
        return volunteers
    }

    @Throws(JSONException::class)
    private fun fillArrayFromJson(jsonArray: JSONArray): List<Volunteer> {
        var jsonObject: JSONObject
        val volunteers: MutableList<Volunteer> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            volunteers.add(Volunteer(jsonObject.getInt("id"), jsonObject.getString("imie"), jsonObject.getString("nazwisko"), false))
        }
        return volunteers
    }
}