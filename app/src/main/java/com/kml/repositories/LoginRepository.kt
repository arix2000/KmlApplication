package com.kml.repositories

import androidx.lifecycle.ViewModel
import com.kml.data.utilities.FileFactory
import com.kml.data.externalDbOperations.DbLogin

class LoginRepository(val fileFactory: FileFactory) : ViewModel() {

    fun checkLoginForResult(login: String, password: String): String {
        val dbLogin = DbLogin(login, password)
        dbLogin.start()
        return dbLogin.result
    }

    fun decideAboutSavingLogData(login: String, password: String, isChecked: Boolean) {
        if (isChecked) {
            fileFactory.saveStateToFile("$login;$password", FileFactory.DATA_TXT)
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

    fun saveSwitchDarkMode(state: String) {
        fileFactory.saveStateToFile(state, FileFactory.LOGIN_KEEP_SWITCH_DARK_MODE_TXT)
    }

    fun getSwitchDarkModeState(): Boolean {
        return fileFactory.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_DARK_MODE_TXT).toBoolean()
    }
}