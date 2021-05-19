package com.kml.data.networking

import com.kml.KmlApp
import com.kml.extensions.logError
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbGetWorksHistory(private val type: String, private val shouldShowAll: Boolean) : ExternalDbHelper() {

    companion object {
        const val GET_WORKS = "GET_WORKS"
        const val GET_MEETINGS = "GET_MEETINGS"
    }

    private val worksFileName = "getWorkHistory.php"
    private val meetingsFileName = "getMeetingsHistory.php"
    private val address = BASE_URL + if(type == GET_WORKS) worksFileName else meetingsFileName
    private var conn: HttpURLConnection? = null

    fun fetchResult(firstName: String = KmlApp.firstName, lastName: String = KmlApp.lastName): String {
        conn = setConnection(address)
        sendData(firstName,lastName)
        return readResult(conn!!)
    }

    private fun sendData(firstName: String, lastName: String) {
        try {
            val outStream = conn?.outputStream
            val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
            val dataToSend = (URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(createNameValue(firstName), "UTF-8")
                    + "&&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(createNameValue(lastName), "UTF-8"))
            writer.write(dataToSend)
            writer.flush()
            writer.close()
            outStream?.close()
        } catch (e: IOException) {
            logError(e)
        }
    }

    private fun createNameValue(baseName: String): String {
        return when(shouldShowAll) {
            true -> {
                if (type == GET_WORKS) "%%" else ""
            }
            false -> baseName
        }
    }
}