package com.kml.viewModels

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kml.Constants.Strings.TODAY
import com.kml.data.utilities.FileFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class WorkAddingViewModelTest {
    private val viewModel = WorkAddingViewModel(FileFactory(ApplicationProvider.getApplicationContext()))

    @Test
    fun decideAboutDate() {
        var result = viewModel.decideAboutDate("12.11.2020")
        assertEquals("12.11.2020", result)

        result = viewModel.decideAboutDate(TODAY)
        assertEquals("27.11.2020", result)
    }

}






