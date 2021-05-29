package com.kml.data.networking

import android.util.Log
import com.kml.KmlApp
import com.kml.models.dto.WorkToAdd
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbSendWork(var work: WorkToAdd) : ExternalDbHelper() {
    private val fileName = "updateCzasPracy.php"

    override fun run() {

        val timeToSend: Float = work.hours + work.minutes.toFloat() / 60
        val address = BASE_URL + fileName
        val httpConnection = setConnection(address)
        try {
            sendData(httpConnection, timeToSend)
            result = readResult(httpConnection!!) == "true"
            invokeOnReceive(result.toString())
        } catch (e: Exception) {
            Log.d("IO_EXCEPTION", "run: " + e.message)
            invokeOnReceive("false")
        }
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
                + "&&" + URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(java.lang.String.valueOf(
            KmlApp.loginId), "UTF-8")
                + "&&" + URLEncoder.encode("nazwaZadania", "UTF-8") + "=" + URLEncoder.encode(work.name, "UTF-8")
                + "&&" + URLEncoder.encode("opisZadania", "UTF-8") + "=" + URLEncoder.encode(work.description, "UTF-8")
                + "&&" + URLEncoder.encode("czasPracyDokladny", "UTF-8") + "=" + URLEncoder.encode(workTimeExact, "UTF-8")
                + "&&" + URLEncoder.encode("imie", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                + "&&" + URLEncoder.encode("nazwisko", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8"))
    }

    var result: Boolean = false
}