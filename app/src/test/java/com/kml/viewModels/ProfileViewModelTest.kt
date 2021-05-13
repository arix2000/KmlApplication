package com.kml.viewModels

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.R
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.koin.test.KoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class ProfileViewModelTest: KoinTest {
    private val viewModel: ProfileViewModel by inject()

    @Test
    fun validateTest() {
        var result = viewModel.validatePassword("old", "")
        assertEquals(R.string.no_empty_fields, result)

        result = viewModel.validatePassword("", "new")
        assertEquals(R.string.no_empty_fields, result)

        result = viewModel.validatePassword("good", "good")
        assertEquals(VALIDATION_SUCCESSFUL, result)

        result = viewModel.validatePassword("old", "this is a text for create more than sixty-four characters. nice  ")
        assertEquals(R.string.too_many_chars, result)
    }

}