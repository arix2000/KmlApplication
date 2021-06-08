package com.kml.repositories

import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.models.dto.Work
import com.kml.models.model.User
import com.kml.repositories.WorksHistoryRepository.Companion.MEETINGS_COMMAND_FOR_ALL
import io.reactivex.rxjava3.core.Single

class VolunteerLogbookRepository(private val restApi: RestApi): BaseRepository() {

    fun fetchVolunteerWorks(user: User): Single<List<Work>> {
        return restApi.fetchWorkHistory(user.firstName, user.lastName)
            .async()
    }

    fun fetchVolunteerMeetings(user: User): Single<List<Work>> {
        return restApi.fetchMeetingsHistory(user.firstName, user.lastName)
            .async()
    }

    fun fetchAllMeetings(): Single<List<Work>> {
        return restApi.fetchMeetingsHistory(MEETINGS_COMMAND_FOR_ALL,MEETINGS_COMMAND_FOR_ALL)
            .async()
    }
}