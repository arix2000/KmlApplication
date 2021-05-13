package com.kml.viewModels

import androidx.lifecycle.ViewModel
import com.kml.extensions.getCurrentYear
import java.text.DateFormatSymbols
import java.util.*

abstract class BrowserVolunteerLogbookViewModel: ViewModel() {

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
        const val SHOW_ALL = "Wszystkie"
        const val SHOW_ALL_ITEM_POSITION = 0
    }
}