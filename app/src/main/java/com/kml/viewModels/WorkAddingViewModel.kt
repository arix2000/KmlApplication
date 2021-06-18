package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.Constants.Strings.TODAY
import com.kml.extensions.getTodayDate
import com.kml.models.dto.WorkToAdd
import com.kml.repositories.WorkAddingRepository
import io.reactivex.rxjava3.core.Single
import java.util.*

class WorkAddingViewModel(
    private val repository: WorkAddingRepository
) : ViewModel() {

    fun sendWorkToDatabase(work: WorkToAdd): Single<Boolean> {
        return repository.addWorkToDatabase(work)
    }

    fun decideAboutDate(date: String): String {
        return if (date == TODAY)
            Calendar.getInstance().getTodayDate()
        else date
    }
}