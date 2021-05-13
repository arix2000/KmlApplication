package com.kml.viewModels

import com.kml.models.User
import com.kml.models.Work
import com.kml.repositories.VolunteerLogbookRepository
import io.reactivex.rxjava3.core.Single

class BrowserVolunteerWorksViewModel(
    private val repository: VolunteerLogbookRepository
) : BrowserVolunteerLogbookViewModel() {

    fun fetchWorks(user: User): Single<List<Work>> {
        return repository.fetchVolunteerWorks(user)
    }
}