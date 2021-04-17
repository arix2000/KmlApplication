package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.extensions.async
import com.kml.models.Volunteer
import com.kml.repositories.VolunteerRepository
import io.reactivex.rxjava3.core.Single
import java.util.*

class VolunteersViewModel : ViewModel() {

    private val repository = VolunteerRepository()

    private var _volunteers: List<Volunteer> = listOf()
    val volunteers: List<Volunteer> get() =  _volunteers

    fun fetchVolunteers(): Single<List<Volunteer>> {
        return repository.fetchVolunteers()
                .async()
                .doOnSuccess { _volunteers = it }
    }

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
}






