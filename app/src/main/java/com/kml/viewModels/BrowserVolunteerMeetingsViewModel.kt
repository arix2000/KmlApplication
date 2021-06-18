package com.kml.viewModels

import com.kml.models.dto.Work
import com.kml.models.model.User
import com.kml.repositories.VolunteerLogbookRepository
import io.reactivex.rxjava3.core.Single

class BrowserVolunteerMeetingsViewModel(
    private val repository: VolunteerLogbookRepository
) : BrowserVolunteerLogbookViewModel() {

    fun fetchMeetings(user: User): Single<List<Work>> {
        return repository.fetchVolunteerMeetings(user)
    }

    fun fetchAllMeetings(): Single<List<Work>>  {
        return repository.fetchAllMeetings()
    }
}