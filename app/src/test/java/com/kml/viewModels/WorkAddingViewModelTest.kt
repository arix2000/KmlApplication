package com.kml.viewModels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.Constants.Strings.TODAY
import com.kml.extensions.getTodayDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.koin.test.AutoCloseKoinTest
import java.util.*

@RunWith(AndroidJUnit4::class)
internal class WorkAddingViewModelTest: AutoCloseKoinTest() {
    private val viewModel: WorkAddingViewModel by inject()

    @Test
    fun decideAboutDate() {
        var result = viewModel.decideAboutDate("12.11.2020")
        assertEquals("12.11.2020", result)

        result = viewModel.decideAboutDate(TODAY)
        assertEquals(Calendar.getInstance().getTodayDate(), result)
    }
}






