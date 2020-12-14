package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer

class TimeManagementViewModel: ViewModel() {

    private val allSelectedVolunteers = mutableSetOf<Volunteer>()
    lateinit var lastTime: TimeToVolunteers

    fun addAll(volunteers: List<Volunteer>) {
        allSelectedVolunteers.addAll(volunteers)
    }
}