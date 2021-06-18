package com.kml.repositories

import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.utilities.FileFactory

class SplashScreenRepository(val fileFactory: FileFactory): BaseRepository() {

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