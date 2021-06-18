package com.kml.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.expect

@RunWith(AndroidJUnit4::class)
class CalendarExtTest : AutoCloseKoinTest() {
    private lateinit var calendar: Calendar
    private lateinit var calendar2: Calendar
    private lateinit var calendar3: Calendar

    @Before
    fun setUp() {
        calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 2000)
        }

        calendar2 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.MONTH, 4)
            set(Calendar.YEAR, 2021)
        }

        calendar3 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 16)
            set(Calendar.MONTH, 1)
            set(Calendar.YEAR, 2021)
        }
    }

    @Test
    fun getTodayDateTest() {
        assertEquals("01.01.2000", calendar.getTodayDate())
        assertEquals("31.05.2021", calendar2.getTodayDate())
        assertEquals("16.02.2021", calendar3.getTodayDate())
    }

    @Test
    fun isNotLastDayInMonthTest() {
        assertTrue { calendar3.isNotLastDayInMonth() }
        assertTrue { calendar.isNotLastDayInMonth() }
        assertFalse { calendar2.isNotLastDayInMonth() }
    }

    @Test
    fun getDaysUntilEndOfThisMonthTest() {
        expect(30) { calendar.getDaysUntilEndOfThisMonth() }
        expect(0) { calendar2.getDaysUntilEndOfThisMonth() }
        expect(12) { calendar3.getDaysUntilEndOfThisMonth() }
    }
}