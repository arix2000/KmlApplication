package com.kml.models

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class WorkTest: AutoCloseKoinTest() {
    lateinit var work: Work
    lateinit var work2: Work

    @Before
    fun setUp() {
        work = Work("Arek","Mądry", "testowanie", "9.9.2020 awjda jwd k alw","","","","Wydarzenia")
        work2 = Work("Arek","Mądry", "testowanie", "01.01.2020 akwhd hak whdj kawh d","","","", "Spotkanie ogólne")
    }

    @Test
    fun isExecutionMonthEquals() {
        assertTrue(work.isExecutionMonthEquals("9"))
        assertTrue(work2.isExecutionMonthEquals("1"))
    }

    @Test
    fun isExecutionYearEquals() {
        assertTrue(work.isExecutionYearEquals("2020"))
        assertTrue(work2.isExecutionYearEquals("2020"))
    }
}