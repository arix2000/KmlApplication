package com.kml.data.externalDbOperations

import com.kml.data.app.KmlApp
import com.kml.extensions.logError
import com.kml.models.WorkToAdd
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbAddingToChosen(private val ids: String,
                       private var volunteersName: String,
                       private val work: WorkToAdd) : ExternalDbHelper() {

    private val fileName = "addTimeOfWorkToChosen.php"
    private val address = BASE_URL + fileName
    var result: String = ""
        get() {
            join(); return field
        }
    private var conn: HttpURLConnection? = null

    override fun run() {
        try {
            conn = setConnection(address)
            sendData()
            result = readResult(conn!!)
        } catch (e: IOException) {
            logError(e)
        }
    }

    fun syncRun(): String =
            try {
                conn = setConnection(address)
                sendData()
                readResult(conn!!)
            } catch (e: IOException) {
                logError(e)
                ""
            }

    private fun convertTimeToSend() = work.hours + work.minutes.toFloat() / 60

    @Throws(IOException::class)
    private fun sendData() {
        val readAbleWorkTime = work.hours.toString() + "h " + work.minutes + "min"
        val outStream = conn!!.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
        val dataToSend = (URLEncoder.encode("workTime", "UTF-8") + "=" + URLEncoder.encode(convertTimeToSend().toString(), "UTF-8")
                + "&&" + URLEncoder.encode("ids", "UTF-8") + "=" + URLEncoder.encode(ids, "UTF-8")
                + "&&" + URLEncoder.encode("workName", "UTF-8") + "=" + URLEncoder.encode(work.name, "UTF-8")
                + "&&" + URLEncoder.encode("volunteersName", "UTF-8") + "=" + URLEncoder.encode(volunteersName, "UTF-8")
                + "&&" + URLEncoder.encode("readAbleWorkTime", "UTF-8") + "=" + URLEncoder.encode(readAbleWorkTime, "UTF-8")
                + "&&" + URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                + "&&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8")
                + "&&" + URLEncoder.encode("meetingDesc", "UTF-8") + "=" + URLEncoder.encode(work.description, "UTF-8"))
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outStream.close()
    }
}