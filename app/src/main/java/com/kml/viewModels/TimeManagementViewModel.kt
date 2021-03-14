package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.Constants
import com.kml.data.models.TimeToVolunteers

class TimeManagementViewModel: ViewModel() {

    lateinit var lastTime: TimeToVolunteers

    fun itemValidation(hours: String, minutes: String): Int {// TODO test!!
        return if(hours.isBlank() || minutes.isBlank())
            Constants.Signal.EMPTY_POOLS
        else if (minutes.toInt()>60)
            Constants.Signal.TOO_MANY_MINUTES
        else Constants.Signal.VALIDATION_SUCCESSFUL
    }
}