package com.kml.repositories

import com.kml.data.networking.DbGetAllUsersData
import com.kml.models.Volunteer
import io.reactivex.rxjava3.core.Single

class VolunteerRepository {
    fun fetchVolunteers(): Single<List<Volunteer>> {
        val dbGetAllUsersData = DbGetAllUsersData()
        return Single.create { it.onSuccess(dbGetAllUsersData.syncRun()) }
    }
}