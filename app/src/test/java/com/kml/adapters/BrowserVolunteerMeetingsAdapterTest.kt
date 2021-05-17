package com.kml.adapters

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.resources.getTestWorkList
import com.kml.resources.getTypesList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import kotlin.test.expect


@RunWith(AndroidJUnit4::class)
class BrowserVolunteerMeetingsAdapterTest : AutoCloseKoinTest() {
    lateinit var adapter: BrowserVolunteerMeetingsAdapter
    lateinit var mockAdapter: BrowserVolunteerMeetingsAdapter

    @Before
    fun beforeTests() {
        adapter = BrowserVolunteerMeetingsAdapter { }
        mockAdapter = mockk()
        every { mockAdapter.filterWorksBy(any(), any(), any(), any()) } returns Unit
        every { mockAdapter.updateWorks(any(), any()) } returns Unit
    }

    @Test
    fun updateWorks() {
        adapter.updateWorks(getTestWorkList(), true)
        expect(true) { adapter.isWorksEmpty() }
        adapter.updateWorks(getTestWorkList(), false)
        expect(false) { adapter.isWorksEmpty() }
    }

    @Test
    fun `FilterWorksBy() should calls updateWorks() `() {
        mockAdapter.updateWorks(getTestWorkList())
        mockAdapter.filterWorksBy("", listOf(), "", "")
        verify { mockAdapter.updateWorks(any()) }
    }

    @Test
    fun `after filterWorksBy() works should be filtered`() {
        adapter.updateWorks(getTestWorkList())
        adapter.filterWorksBy("Wszystkie", getTypesList(), "5", "2021")
        expect(5) { adapter.itemCount }
        adapter.filterWorksBy("Spotkania sekcji", getTypesList(), "5", "2021")
        expect(4) { adapter.itemCount }
        adapter.filterWorksBy("Wszystkie", getTypesList(), "0", "2021")
        expect(125) { adapter.itemCount }
        adapter.filterWorksBy("Spotkania do wydarzeń", getTypesList(), "0", "2021")
        expect(1) { adapter.itemCount }
    }

    @Test
    fun `should return correct total works count`() {
        adapter.updateWorks(getTestWorkList())
        adapter.filterWorksBy("Wszystkie", getTypesList(), "0", "2021")
        expect("183h 18min") { adapter.getWorksTimeTotal() }
    }
}