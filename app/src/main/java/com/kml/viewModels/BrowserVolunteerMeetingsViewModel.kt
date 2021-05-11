package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.models.User
import com.kml.models.Work
import com.kml.repositories.BrowserVolunteerMeetingsRepository
import io.reactivex.rxjava3.core.Single

class BrowserVolunteerMeetingsViewModel(
    private val repository: BrowserVolunteerMeetingsRepository
) : ViewModel() {

    fun fetchMeetings(user: User): Single<List<Work>> {
        return repository.fetchVolunteerMeetings(user)
    }
}