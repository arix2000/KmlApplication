package com.kml.data.externalDbOperations

import android.util.Log
import com.kml.data.app.KmlApp
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbGetWorksHistory : ExternalDbHelper() {
    private val fileName = "getWorkHistory.php"
    private val address = BASE_URL + fileName
    private var conn: HttpURLConnection? = null

    var result: String = ""
        get() {
            join(); return field
        }

    override fun run() {
        conn = setConnection(address)
        sendData()
        result = readResult(conn!!)
    }

    private fun sendData() {
        try {
            val outStream = conn?.outputStream
            val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
            val dataToSend = (URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                    + "&&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8"))
            writer.write(dataToSend)
            writer.flush()
            writer.close()
            outStream?.close()
        } catch (e: IOException) {
            Log.d(IO_EXCEPTION_TAG, "sendData: " + e.message)
        }
    }
}