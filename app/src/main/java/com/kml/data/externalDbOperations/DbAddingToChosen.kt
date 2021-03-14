package com.kml.data.externalDbOperations

import android.util.Log
import com.kml.data.app.KmlApp
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder
import kotlin.math.roundToInt

class DbAddingToChosen(private val ids: String,
                       private var volunteersName: String,
                       private val workName: String,
                       val minutes: Int,
                       val hours: Int,
                       private val meetingDesc: String) : ExternalDbHelper() {

    private val fileName = "addTimeOfWorkToChosen.php"
    private val address = BASE_URL + fileName
    var result: String = ""
        get() {
            join(); return field
        }
    private var conn: HttpURLConnection? = null

    init {
        volunteersName = "Dodano godziny wybranym: $volunteersName"
    }

    override fun run() {
        try {
            conn = setConnection(address)
            sendData()
            result = readResult(conn!!)
        } catch (e: IOException) {
            Log.d("IO_EXCEPTION", "run: " + e.message)
        }
    }

    private fun convertTimeToSend(): Float {
        var workTime = hours + minutes.toFloat() / 60
        workTime *= 100
        workTime = workTime.roundToInt().toFloat()
        workTime /= 100
        return workTime
    }

    @Throws(IOException::class)
    private fun sendData() {
        val readAbleWorkTime = hours.toString() + "h " + minutes + "min"
        val outStream = conn!!.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
        val dataToSend = (URLEncoder.encode("workTime", "UTF-8") + "=" + URLEncoder.encode(convertTimeToSend().toString(), "UTF-8")
                + "&&" + URLEncoder.encode("ids", "UTF-8") + "=" + URLEncoder.encode(ids, "UTF-8")
                + "&&" + URLEncoder.encode("workName", "UTF-8") + "=" + URLEncoder.encode(workName, "UTF-8")
                + "&&" + URLEncoder.encode("volunteersName", "UTF-8") + "=" + URLEncoder.encode(volunteersName, "UTF-8")
                + "&&" + URLEncoder.encode("readAbleWorkTime", "UTF-8") + "=" + URLEncoder.encode(readAbleWorkTime, "UTF-8")
                + "&&" + URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                + "&&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8")
                + "&&" + URLEncoder.encode("meetingDesc", "UTF-8") + "=" + URLEncoder.encode(meetingDesc, "UTF-8"))
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outStream.close()
    }
}