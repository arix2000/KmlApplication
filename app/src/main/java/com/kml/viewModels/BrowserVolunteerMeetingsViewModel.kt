package com.kml.viewModels

import com.kml.models.User
import com.kml.models.Work
import com.kml.repositories.VolunteerLogbookRepository
import io.reactivex.rxjava3.core.Single

class BrowserVolunteerMeetingsViewModel(
    private val repository: VolunteerLogbookRepository
) : BrowserVolunteerLogbookViewModel() {

    fun fetchMeetings(user: User): Single<List<Work>> {
        return repository.fetchVolunteerMeetings(user)
    }
}