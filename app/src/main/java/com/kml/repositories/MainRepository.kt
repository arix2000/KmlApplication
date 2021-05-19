package com.kml.repositories

import com.kml.utilities.FileFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(private val fileFactory: FileFactory): BaseRepository() {

    fun saveSwitchDarkMode(state: String) {
        fileFactory.saveStateToFile(state, FileFactory.LOGIN_KEEP_SWITCH_DARK_MODE_TXT)
    }

    fun getSwitchDarkModeState(): Boolean {
        return fileFactory.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_DARK_MODE_TXT).toBoolean()
    }

    fun clearLogData() {
        CoroutineScope(Dispatchers.IO).launch {
            fileFactory.clearFileState(FileFactory.DATA_TXT)
        }
    }
}