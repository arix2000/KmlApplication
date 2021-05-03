package com.kml.repositories

import androidx.lifecycle.ViewModel
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.data.utilities.FileFactory

class SplashScreenRepository(val fileFactory: FileFactory) : ViewModel() {

    fun getLogDataIfExist(): String {
        if (fileFactory.readFromFile(FileFactory.DATA_TXT).contains(";")) {
            return fileFactory.readFromFile(FileFactory.DATA_TXT)
        }
        return EMPTY_STRING
    }

    fun getSwitchDarkModeState(): Boolean {
        return fileFactory.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_DARK_MODE_TXT).toBoolean()
    }
}