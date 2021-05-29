package com.kml.repositories

import com.kml.data.networking.DbGetAllUsersData
import com.kml.models.dto.Volunteer
import io.reactivex.rxjava3.core.Single

class VolunteerRepository: BaseRepository() {
    fun fetchVolunteers(): Single<List<Volunteer>> {
        val dbGetAllUsersData = DbGetAllUsersData()
        return Single.create { it.onSuccess(dbGetAllUsersData.syncRun()) }
    }
}