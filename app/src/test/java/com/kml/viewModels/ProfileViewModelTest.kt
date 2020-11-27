package com.kml.viewModels

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.R
import com.kml.data.utilities.FileFactory
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class ProfileViewModelTest {
    private val viewModel = ProfileViewModel(FileFactory(ApplicationProvider.getApplicationContext()))

    @Test
    fun validateTest() {
        var result = viewModel.validatePassword("old", "")
        assertEquals(R.string.no_empty_fields, result)

        result = viewModel.validatePassword("", "new")
        assertEquals(R.string.no_empty_fields, result)

        result = viewModel.validatePassword("good", "good")
        assertEquals(ProfileViewModel.VALIDATION_SUCCESSFUL, result)

        result = viewModel.validatePassword("old", "this is a text for create more than sixty-four characters. nice  ")
        assertEquals(R.string.too_many_chars, result)
    }

    @Test
    fun getDataFromFile() {
        val result = viewModel.getProfileData()
        viewModel.saveProfileValues(result)
    }

}