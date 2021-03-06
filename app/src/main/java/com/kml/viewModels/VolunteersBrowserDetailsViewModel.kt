package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.models.dto.Profile
import com.kml.repositories.VolunteersBrowserDetailsRepository
import io.reactivex.rxjava3.core.Single

class VolunteersBrowserDetailsViewModel(
    private val repository: VolunteersBrowserDetailsRepository
) : ViewModel() {
    var profile = Profile.EMPTY_PROFILE

    fun fetchVolunteerData(id: Int): Single<Profile> {
        return repository.fetchVolunteersData(id)
                .doOnSuccess { profile = it }
    }
}