package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.models.Profile
import com.kml.repositories.VolunteersBrowserDetailsRepository
import io.reactivex.rxjava3.core.Single

class VolunteersBrowserDetailsViewModel : ViewModel() {
    private val repository = VolunteersBrowserDetailsRepository()

    fun fetchVolunteerData(id: Int): Single<Profile> {
        return repository.fetchVolunteersData(id)
                .map { Profile.createFrom(it) }
    }
}