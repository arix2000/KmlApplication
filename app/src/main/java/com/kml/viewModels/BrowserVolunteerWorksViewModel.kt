package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.extensions.getCurrentYear
import com.kml.models.User
import com.kml.models.Work
import com.kml.repositories.BrowserVolunteerWorksRepository
import io.reactivex.rxjava3.core.Single
import java.text.DateFormatSymbols
import java.util.*

class BrowserVolunteerWorksViewModel : ViewModel() {
    val repository = BrowserVolunteerWorksRepository()

    fun fetchWorks(user: User): Single<List<Work>> {
        return repository.fetchVolunteerWorks(user)
    }

    fun getYearList(): List<String> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years: MutableList<String> = mutableListOf()
        val range = currentYear - LAST_YEARS_POSITION..currentYear
        for (year in range) {
            years.add(year.toString())
        }
        return years
    }

    fun getMonthList(isCurrentYear: Boolean): List<String> {
        return if (isCurrentYear) {
            val monthNow = Calendar.getInstance().get(Calendar.MONTH)
            DateFormatSymbols(Locale.getDefault()).months.toMutableList().apply {
                add(0, SHOW_ALL)
                removeAll { indexOf(it) > monthNow.inc() }
            }
        } else {
            DateFormatSymbols(Locale.getDefault()).months.toMutableList().apply {
                add(SHOW_ALL_ITEM_POSITION, SHOW_ALL)
            }
        }
    }

    fun isCurrentYear(selectedYear: Int): Boolean {
        val currentYear = Calendar.getInstance().getCurrentYear()
        return currentYear == selectedYear
    }

    companion object {
        /** This is last position in array of years. Note that amount of years it's that value plus one */
        const val LAST_YEARS_POSITION = 3
        const val SHOW_ALL = "Pokaż wszystko"
        const val SHOW_ALL_ITEM_POSITION = 0
    }
}