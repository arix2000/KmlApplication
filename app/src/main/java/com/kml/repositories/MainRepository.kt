package com.kml.repositories

import androidx.datastore.preferences.core.edit
import com.kml.Constants.Keys.IS_FROM_NOTIFICATION_KEY
import com.kml.utilities.FileFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun getSavedIsFromNotification(): Flow<Boolean?> {
        return dataStore.data.map { return@map it[IS_FROM_NOTIFICATION_KEY] }
    }

    fun clearIsFromNotification() {
        ioScope.launch { dataStore.edit { it[IS_FROM_NOTIFICATION_KEY] = false }}
    }
}