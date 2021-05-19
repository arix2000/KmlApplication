package com.kml.repositories

import com.kml.data.networking.DbGetWorksHistory
import com.kml.extensions.async
import com.kml.extensions.createWorkListFrom
import com.kml.models.User
import com.kml.models.Work
import io.reactivex.rxjava3.core.Single

class VolunteerLogbookRepository: BaseRepository() {

    fun fetchVolunteerWorks(user: User): Single<List<Work>> {
        return Single.create<String> {
            it.onSuccess(
                DbGetWorksHistory(DbGetWorksHistory.GET_WORKS, false)
                    .fetchResult(user.firstName, user.lastName)
            )
        }.map { createWorkListFrom(it) }.async()
    }

    fun fetchVolunteerMeetings(user: User): Single<List<Work>> {
        return Single.create<String> {
            it.onSuccess(
                DbGetWorksHistory(DbGetWorksHistory.GET_MEETINGS, false)
                    .fetchResult(user.firstName, user.lastName)
            )
        }.map { createWorkListFrom(it) }.async()
    }
}