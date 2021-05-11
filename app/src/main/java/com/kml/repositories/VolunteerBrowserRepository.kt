package com.kml.repositories

import com.kml.data.networking.DbGetAllUsersData
import com.kml.models.Volunteer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


class VolunteerBrowserRepository {

    fun fetchVolunteers(): Single<List<Volunteer>> {
        return Single.create<List<Volunteer>>  {
            it.onSuccess(DbGetAllUsersData().syncRun())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}