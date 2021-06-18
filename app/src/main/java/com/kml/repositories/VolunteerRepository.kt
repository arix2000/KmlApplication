package com.kml.repositories

import com.kml.data.networking.RestApi
import com.kml.models.dto.Volunteer
import io.reactivex.rxjava3.core.Single

class VolunteerRepository(private val restApi: RestApi): BaseRepository() {
    fun fetchVolunteers(): Single<List<Volunteer>> {
        return restApi.fetchVolunteers()
    }
}