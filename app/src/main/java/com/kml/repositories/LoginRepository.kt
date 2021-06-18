package com.kml.repositories

import com.kml.KmlApp
import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single

class LoginRepository(
    val fileFactory: FileFactory,
    private val restApi: RestApi
) : BaseRepository() {

    fun fetchLoginResult(login: String, password: String): Single<String> {
        return restApi.logIn(login, password).async()
    }

    fun decideAboutSavingLogData(login: String, password: String, isChecked: Boolean) {
        if (isChecked) {
            fileFactory.saveStateToFile("${KmlApp.loginId};$login;$password", FileFactory.DATA_TXT)
        } else {
            fileFactory.clearFileState(FileFactory.DATA_TXT) //clear File
        }
        fileFactory.saveStateToFile(isChecked.toString(), FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT)
    }

    fun getLogDataIfExist(): String {
        if (fileFactory.readFromFile(FileFactory.DATA_TXT).contains(";")) {
            return fileFactory.readFromFile(FileFactory.DATA_TXT)
        }
        return ""
    }

    fun getSwitchState(): String {
        return fileFactory.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT)
    }
}