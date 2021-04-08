package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.models.Volunteer
import com.kml.repositories.VolunteerBrowserRepository
import io.reactivex.rxjava3.core.Single

class VolunteersBrowserViewModel : ViewModel() {
    private val repository = VolunteerBrowserRepository()
    var scrollState = 0

    fun fetchVolunteers(): Single<List<Volunteer>> {
        return repository.fetchVolunteers()
    }

    override fun onCleared() {
        super.onCleared()
    }

}