package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.data.utilities.Signal

class TimeManagementViewModel: ViewModel() {

    private val allSelectedVolunteers = mutableSetOf<Volunteer>()
    lateinit var lastTime: TimeToVolunteers

    fun addAll(volunteers: List<Volunteer>) {
        allSelectedVolunteers.addAll(volunteers)
    }

    fun itemValidation(hours: String, minutes: String): Int {// TODO test!!
        return if(hours.isBlank() || minutes.isBlank())
            Signal.EMPTY_POOLS
        else if (minutes.toInt()>60)
            Signal.TOO_MANY_MINUTES
        else Signal.VALIDATION_SUCCESSFUL

    }
}