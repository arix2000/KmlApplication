package com.kml.data.utilities

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class FileFactory(private val context: Context) {

    companion object {
        const val CURRENT_TIME_TXT = "currentTime.txt"
        const val DATA_TXT = "data.txt"
        const val PROFILE_PHOTO_PATH_TXT = "profilePhotoPath.txt"
        const val PROFILE_KEEP_DATA_TXT = "profileKeepData.txt"
        const val HISTORY_KEEP_DATA_TXT = "historyKeepData.txt"
        const val LOGIN_KEEP_SWITCH_CHOICE_TXT = "loginKeepSwitchChoice.txt"
        const val LOGIN_KEEP_SWITCH_DARK_MODE_TXT = "loginKeepSwitchDarkMode.txt"
    }

    fun saveStateToFile(toSave: String, filename: String) {
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(toSave.toByteArray())
        fos.close()
    }

    fun clearFileState(filename: String) {
        saveStateToFile("", filename)
    }

    fun readFromFile(filename: String): String {
        val sb = StringBuilder()

        val fis = context.openFileInput(filename)
        val isr = InputStreamReader(fis)
        val br = BufferedReader(isr)
        var content: String?
        while (br.readLine().also { content = it } != null) {
            sb.append(content)
        }
        fis.close()

        return sb.toString()
    }
}