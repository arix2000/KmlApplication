package com.kml.adapters

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.resources.getTestWorkList
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.expect

@RunWith(AndroidJUnit4::class)
class BrowserVolunteerMeetingsAdapterTest {
    val adapter = BrowserVolunteerMeetingsAdapter {}

    @Test
    fun updateWorks() {
        adapter.updateWorks(getTestWorkList(), true)
        expect(true) { adapter.isWorksEmpty() }
        adapter.updateWorks(getTestWorkList(), false)
        expect(false) { adapter.isWorksEmpty() }
    }

    @Test
    fun filterWorksBy() {
//TODO should check if filtering is correctly and is used updateWorksMethod to achieve updating data
    }
}