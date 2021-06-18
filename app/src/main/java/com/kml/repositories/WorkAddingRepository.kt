package com.kml.repositories

import com.kml.KmlApp
import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.models.dto.WorkToAdd
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single

class WorkAddingRepository(
    val fileFactory: FileFactory,
    private val restApi: RestApi
) : BaseRepository() {

    fun addWorkToDatabase(work: WorkToAdd): Single<Boolean> {
        with(work) {
            return restApi.addWork(
                KmlApp.loginId.toString(),
                getWorkTimeFloat().toString(),
                name,
                description,
                getWorkTimeReadable(),
                KmlApp.firstName, KmlApp.lastName
            ).async()
        }
    }
}