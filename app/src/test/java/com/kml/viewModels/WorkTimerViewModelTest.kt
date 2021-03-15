package com.kml.viewModels

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.models.Time
import com.kml.models.WorkToAdd
import com.kml.data.utilities.FileFactory
import com.kml.data.utilities.Signal
import com.kml.views.dialogs.InstantAddWorkDialog.Companion.TODAY
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class WorkTimerViewModelTest {
    private val viewModel = WorkTimerViewModel(FileFactory(ApplicationProvider.getApplicationContext()))

    @Test
    fun validateWork() {
        var result = viewModel.validateWork("workName","")
        assertFalse(result)

        result = viewModel.validateWork("","workDescription")
        assertFalse(result)

        result = viewModel.validateWork("workName","workDescription")
        assertTrue(result)
    }

    @Test
    fun validateWorkInstant() {
        var result = viewModel.validateWorkInstant(WorkToAdd("test", "test", 0, 0))
        assertEquals(Signal.VALIDATION_SUCCESSFUL, result)

        result = viewModel.validateWorkInstant(WorkToAdd("test", "test", 0, 61))
        assertEquals(Signal.TOO_MANY_MINUTES, result)

        result = viewModel.validateWorkInstant(WorkToAdd("test", "test", -1, 0))
        assertEquals(Signal.EMPTY_POOLS, result)
    }

    @Test
    fun getTime() {
        viewModel.secondsValue = 60
        viewModel.minutes = 9
        viewModel.hours = 9
        val result = viewModel.getTime()

        assertEquals(Time("09:","10:","00"), result)
    }

    @Test
    fun decideAboutDate()
    {
        var result = viewModel.decideAboutDate("12.11.2020")
        assertEquals("12.11.2020", result)

        result = viewModel.decideAboutDate(TODAY)
        assertEquals("27.11.2020", result)
    }

}






