package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.extensions.async
import com.kml.models.dto.Volunteer
import com.kml.repositories.VolunteerRepository
import io.reactivex.rxjava3.core.Single
import java.util.*

class VolunteersViewModel(
    private val repository: VolunteerRepository
) : ViewModel() {

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
            if (volunteer.firstName.lowercase(Locale.ROOT).contains(typedText.lowercase(Locale.ROOT))) {
                filteredVolunteers.add(volunteer)
            }
        }
        return filteredVolunteers
    }

    fun setCheckedVolunteersDisabled() {
        volunteers.forEach {
            if (it.isChecked) it.isDisabled = true
        }
    }
}






