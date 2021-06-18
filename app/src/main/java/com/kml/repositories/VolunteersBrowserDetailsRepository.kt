package com.kml.repositories

import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.models.dto.Profile
import io.reactivex.rxjava3.core.Single

class VolunteersBrowserDetailsRepository(private val restApi: RestApi): BaseRepository() {

    fun fetchVolunteersData(id: Int): Single<Profile> {
        return restApi.fetchUserInfo(id).async()

    }
}