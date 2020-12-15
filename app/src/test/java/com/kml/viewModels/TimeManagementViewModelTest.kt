package com.kml.viewModels

import com.kml.R
import com.kml.data.utilities.Signal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TimeManagementViewModelTest
{
    private val viewModel = TimeManagementViewModel()

    @Test
    fun itemValidation()
    {
        var actual = viewModel.itemValidation("","")
        assertEquals(R.string.no_empty_fields,actual)
        actual = viewModel.itemValidation("120","")
        assertEquals(R.string.no_empty_fields,actual)
        actual = viewModel.itemValidation("","120")
        assertEquals(R.string.no_empty_fields,actual)

        actual = viewModel.itemValidation("12","61")
        assertEquals(R.string.too_many_minutes,actual)

        actual = viewModel.itemValidation("0","60")
        assertEquals(Signal.VALIDATION_SUCCESSFUL,actual)
        actual = viewModel.itemValidation("0","0")
        assertEquals(Signal.VALIDATION_SUCCESSFUL,actual)

    }
}