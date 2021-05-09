package com.kml.viewModels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.viewModels.BrowserVolunteerWorksViewModel.Companion.LAST_YEARS_POSITION
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
internal class BrowserVolunteerWorksViewModelTest {
    private val viewModel = BrowserVolunteerWorksViewModel()

    @Test
    fun getYearList() {
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)

        val result = viewModel.getYearList()
        result.forEach {
            assertDoesNotThrow { it.toInt() }
        }
        assertTrue(result.isNotEmpty())
        assertEquals(result.size, LAST_YEARS_POSITION.inc())
        assertEquals(result.last(), (yearNow).toString())
    }

    @Test
    fun getMonthList() {
        val monthNow = Calendar.getInstance().get(Calendar.MONTH)
        var result = viewModel.getMonthList(true)
        assertEquals(result.lastIndex, monthNow)

        result = viewModel.getMonthList(false)
        assertEquals(result.lastIndex, 12)
    }
}