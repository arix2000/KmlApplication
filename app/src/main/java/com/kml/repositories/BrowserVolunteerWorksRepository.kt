package com.kml.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.data.networking.DbGetWorksHistory
import com.kml.data.networking.DbGetWorksHistory.Companion.GET_WORKS
import com.kml.extensions.async
import com.kml.models.User
import com.kml.models.Work
import io.reactivex.rxjava3.core.Single


class BrowserVolunteerWorksRepository {

    fun fetchVolunteerWorks(user: User): Single<List<Work>> {
        return Single.create<String> {
            it.onSuccess(
                    DbGetWorksHistory(GET_WORKS, false)
                            .fetchResult(user.firstName, user.lastName)
            )
        }.map { createListFromJson(it) }.async()
    }

    private fun createListFromJson(json: String): List<Work> {
        if (json.isBlank())
            return arrayListOf()

        val type = object : TypeToken<List<Work>>() {}.type
        return Gson().fromJson(json, type)
    }
}
