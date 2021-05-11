package com.kml.repositories

import com.kml.data.networking.DbGetUserData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class VolunteersBrowserDetailsRepository {

    fun fetchVolunteersData(id: Int): Single<String> {
        return Single.create<String> {
            it.onSuccess(DbGetUserData().syncRun(id))
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }
}