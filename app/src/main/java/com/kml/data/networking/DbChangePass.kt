package com.kml.data.networking

import com.kml.extensions.logError
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbChangePass(private val newPassword: String, private val oldPassword: String, private val loginId: Int) : ExternalDbHelper() {

    companion object {
        const val CHANGE_SUCCESSFUL = "Pomyślnie zmieniono hasło!"
        const val CHANGE_FAILED = "Coś poszło nie tak!"
    }

    private val fileName = "changePass.php"
    private var result: String = ""

    override fun run() {
        val address = BASE_URL + fileName
        try {
            val conn = setConnection(address)
            sendData(conn)
            result = readResult(conn)

            result = when (result) {
                "1" -> CHANGE_SUCCESSFUL
                "0" -> CHANGE_FAILED
                else -> result
            }

            invokeOnReceive(result)
        } catch (e: IOException) {
            logError(e)
        }
    }

    @Throws(IOException::class)
    private fun sendData(conn: HttpURLConnection?) {
        val outSteam = conn?.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outSteam, "UTF-8"))
        val dataToSend = (URLEncoder.encode("newPassword", "UTF-8") + "=" + URLEncoder.encode(newPassword, "UTF-8")
                + "&&" + URLEncoder.encode("oldPassword", "UTF-8") + "=" + URLEncoder.encode(oldPassword, "UTF-8")
                + "&&" + URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(loginId.toString(), "UTF-8"))
        writer.write(dataToSend)
        writer.flush()
        writer.close()
        outSteam?.close()
    }
}