package com.kml.repositories

import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(
    private val fileFactory: FileFactory,
    private val restApi: RestApi
    ): BaseRepository() {

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

    fun fetchAdminIds(): Single<List<Int>> {
        return restApi.fetchAdminIds()
            .async()
    }

}