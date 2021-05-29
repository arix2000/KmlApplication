package com.kml.repositories

import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.kml.Constants.Keys.WORK_TO_ADD_KEY
import com.kml.data.networking.DbAddingToChosen
import com.kml.models.dto.WorkToAdd
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SummaryVolunteerRepository: BaseRepository() {
    private lateinit var addingToChosen: DbAddingToChosen

    fun sendWorkToDb(ids: String, volunteersNames: String, work: WorkToAdd): Single<Boolean> {
        addingToChosen = DbAddingToChosen(ids, volunteersNames, work)
        return Single.create { it.onSuccess(addingToChosen.syncRun() == "true") }
    }

    fun cacheWork(work: WorkToAdd) {
        CoroutineScope(IO).launch {
            dataStore.edit {
                it[WORK_TO_ADD_KEY] = Gson().toJson(work)
            }
        }
    }

    fun clearCache() {
        CoroutineScope(IO).launch {
            dataStore.edit {
                it.remove(WORK_TO_ADD_KEY)
            }
        }
    }

    fun getSavedWork(): Flow<WorkToAdd?> {
        return dataStore.data.map { return@map Gson().fromJson(it[WORK_TO_ADD_KEY], WorkToAdd::class.java) }
    }
}