package com.kml.data.externalDbOperations

import android.util.Log
import com.kml.data.app.KmlApp
import com.kml.data.models.WorkToAdd
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URLEncoder
import kotlin.math.roundToInt

class DbSendWork(var work: WorkToAdd) : ExternalDbHelper() {
    private val fileName = "updateCzasPracy.php"

    override fun run() {

        var timeToSend: Float = work.hours + work.minutes.toFloat() / 60
        timeToSend = roundToTwoDecimalPoint(timeToSend)
        val address = BASE_URL + fileName
        val httpConnection = setConnection(address)
        try {
            sendData(httpConnection, timeToSend)
            result = readResult(httpConnection!!) == "true"
        } catch (e: IOException) {
            Log.d("IO_EXCEPTION", "run: " + e.message)
        }
    }

    private fun roundToTwoDecimalPoint(timeToSend: Float): Float {
        var timeToSend = timeToSend
        timeToSend *= 100
        timeToSend = timeToSend.roundToInt().toFloat()
        timeToSend /= 100
        return timeToSend
    }

    @Throws(IOException::class)
    private fun sendData(connection: HttpURLConnection?, timeToSend: Float) {
        val outStream = connection?.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
        val dataToSend = setDataToSend(timeToSend)
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outStream?.close()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun setDataToSend(timeToSend: Float): String {
        val workTimeExact = work.hours.toString() + "h " + work.minutes + "min"
        return (URLEncoder.encode("czasPracy", "UTF-8") + "=" + URLEncoder.encode(timeToSend.toString(), "UTF-8")
                + "&&" + URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(java.lang.String.valueOf(KmlApp.loginId), "UTF-8")
                + "&&" + URLEncoder.encode("nazwaZadania", "UTF-8") + "=" + URLEncoder.encode(work.name, "UTF-8")
                + "&&" + URLEncoder.encode("opisZadania", "UTF-8") + "=" + URLEncoder.encode(work.description, "UTF-8")
                + "&&" + URLEncoder.encode("czasPracyDokladny", "UTF-8") + "=" + URLEncoder.encode(workTimeExact, "UTF-8")
                + "&&" + URLEncoder.encode("imie", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                + "&&" + URLEncoder.encode("nazwisko", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8"))
    }

    var result: Boolean = false
        get() {
            this.join()
            return field
        }
}