package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.Constants.Strings.TODAY
import com.kml.extensions.getTodayDate
import com.kml.models.WorkToAdd
import com.kml.repositories.WorkAddingRepository
import java.util.*

class WorkAddingViewModel(
    private val repository: WorkAddingRepository
) : ViewModel() {

    fun sendWorkToDatabase(work: WorkToAdd, onReceived: (Boolean)->Unit) {
        return repository.addWorkToDatabase(work, onReceived)
    }

    fun decideAboutDate(date: String): String {
        return if (date == TODAY)
            Calendar.getInstance().getTodayDate()
        else date
    }
}