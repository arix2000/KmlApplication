package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kml.Constants
import com.kml.extensions.async
import com.kml.extensions.getTodayDate
import com.kml.models.dto.Volunteer
import com.kml.models.dto.WorkToAdd
import com.kml.repositories.SummaryVolunteerRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SummaryVolunteerViewModel(
    private val repository: SummaryVolunteerRepository
) : ViewModel() {

    lateinit var chosenVolunteers: List<Volunteer>
    var isAllVolunteersChosen = false
    private val savedWorkLiveData by lazy { MutableLiveData<WorkToAdd>() }

    fun createReadableFromVolunteers(): String {
        val chosenVolunteersMerged = createStringFromVolunteers()
        return chosenVolunteersMerged.substring(0, chosenVolunteersMerged.length - 2) + "."
    }

    private fun createStringFromVolunteers(): String {
        val stringBuilder = StringBuilder()
        for (volunteer in chosenVolunteers) {
            val oneVolunteer = volunteer.firstName + " " + volunteer.lastName + ", "
            stringBuilder.append(oneVolunteer)
        }
        return stringBuilder.toString()
    }

    fun addWorkToDatabase(work: WorkToAdd): Single<Boolean> {
        val ids = getIdsStringFromVolunteers()
        val volunteersNames = createStringFromVolunteers()
        return repository.sendWorkToDb(ids, volunteersNames, work)
                .async()
    }

    private fun getIdsStringFromVolunteers(): String {
        val ids = StringBuilder()
        for (volunteer in chosenVolunteers) {
            ids.append(volunteer.id.toString() + ",")
        }
        ids.replace(ids.length - 1, ids.length, "")
        return ids.toString()
    }

    fun decideAboutDate(date: String): String {
        return if (date == Constants.Strings.TODAY)
            Calendar.getInstance().getTodayDate()
        else date
    }

    fun fetchSavedWork() {
        viewModelScope.launch {
            repository.getSavedWork().collect {
                it?.let { savedWorkLiveData.value = it }
            }
        }
    }

    fun cacheWork(work: WorkToAdd) {
        repository.cacheWork(work)
    }

    fun clearCache() {
        repository.clearCache()
    }

    fun getSavedWork() = savedWorkLiveData
}