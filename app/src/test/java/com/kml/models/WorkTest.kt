package com.kml.models

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkTest {
    val work = Work("Arek","Mądry", "testowanie", "9.9.2020 awjda jwd k alw","","","")
    val work2 = Work("Arek","Mądry", "testowanie", "01.01.2020 akwhd hak whdj kawh d","","","")

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