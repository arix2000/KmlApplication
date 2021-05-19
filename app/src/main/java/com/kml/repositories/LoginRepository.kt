package com.kml.repositories

import androidx.lifecycle.ViewModel
import com.kml.KmlApp
import com.kml.data.networking.DbLogin
import com.kml.utilities.FileFactory
import com.kml.extensions.async
import io.reactivex.rxjava3.core.Single

class LoginRepository(val fileFactory: FileFactory) : ViewModel() {

    fun fetchLoginResult(login: String, password: String): Single<String> {
        val dbLogin = DbLogin(login, password)
        return Single.create<String> { it.onSuccess(dbLogin.syncRun()) }
            .async()
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