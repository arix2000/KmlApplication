package com.kml.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import java.util.*

@RunWith(AndroidJUnit4::class)
class BaseUtilsKtTest: AutoCloseKoinTest() {
    lateinit var calendar:Calendar

    @Before
    fun setUp() {
        calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 2000)
        }
    }

    @Test
    fun getTodayDate() {

        val result = calendar.getTodayDate()
        assertEquals("01.01.2000",result)
    }
}