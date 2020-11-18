package com.kml.data.externalDbOperations

import android.util.Log
import com.kml.data.app.KmlApp
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
            sendData(connection)
            result = readResult(connection!!)
        } catch (e: Exception) {
            Log.e("DB_GET_USERDATA_ERROR", "run: " + e.message, e)
        }
    }

    @Throws(IOException::class)
    private fun sendData(connection: HttpURLConnection?) {
        val outStream = connection!!.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
        val dataToSend = URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(java.lang.String.valueOf(KmlApp.loginId), "UTF-8")
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outStream.close()
    }
}