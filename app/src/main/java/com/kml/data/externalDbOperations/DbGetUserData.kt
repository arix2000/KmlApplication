package com.kml.data.externalDbOperations

import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.data.app.KmlApp
import com.kml.extensions.logError
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbGetUserData : ExternalDbHelper() {
    private val fileName = "getDataAboutUser.php"
    var result: String = ""
        get() {
            this.join()
            return field
        }

    override fun run() {
        val address = BASE_URL + fileName
        try {
            val connection = setConnection(address)
            sendData(connection, KmlApp.loginId)
            result = readResult(connection!!)
        } catch (e: Exception) {
            logError(e)
        }
    }

    fun syncRun(id: Int): String {
        val address = BASE_URL + fileName
        return try {
            val connection = setConnection(address)
            sendData(connection, id)
            readResult(connection!!)
        } catch (e: Exception) {
            logError(e)
            EMPTY_STRING
        }
    }

    @Throws(IOException::class)
    private fun sendData(connection: HttpURLConnection?, id: Int) {
        val outStream = connection!!.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
        val dataToSend = URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(id.toString(), "UTF-8")
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outStream.close()
    }
}