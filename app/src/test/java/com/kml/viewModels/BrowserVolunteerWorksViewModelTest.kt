package com.kml.viewModels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.viewModels.BrowserVolunteerLogbookViewModel.Companion.LAST_YEARS_POSITION
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.koin.test.AutoCloseKoinTest
import java.util.*
import kotlin.test.fail

@RunWith(AndroidJUnit4::class)
internal class BrowserVolunteerWorksViewModelTest: AutoCloseKoinTest() {
    private val viewModel :BrowserVolunteerWorksViewModel by inject()

    @Test
    fun getYearList() {
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)

        val result = viewModel.getYearList()
        result.forEach {
            try {
                it.toInt()
            } catch (e: Exception) {
                fail("Should not throw any exception")
            }
        }
        assertTrue(result.isNotEmpty())
        assertEquals(result.size, LAST_YEARS_POSITION.inc())
        assertEquals(result.last(), (yearNow).toString())
    }

    @Test
    fun getMonthList() {
        val monthNow = Calendar.getInstance().get(Calendar.MONTH)
        var result = viewModel.getMonthList(true)
        assertEquals(result.lastIndex.dec(), monthNow)

        result = viewModel.getMonthList(false)
        assertEquals(result.lastIndex.dec(), 12)
    }
}