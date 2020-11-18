package com.kml.data.externalDbOperations

import android.util.Log
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder

class DbLogin(private val login: String, private val password: String) : ExternalDbHelper() {
    private val fileName = "login.php"
    private var httpConnection: HttpURLConnection? = null
    var result: String = ""
    get() { join(); return field}
    private val address: String = BASE_URL + fileName


    override fun run() {
        httpConnection = setConnection(address)
        sendData()
        result = readResult(httpConnection!!)
    }

    private fun sendData() {
        try {
            val outStream = httpConnection?.outputStream
            val writer = BufferedWriter(OutputStreamWriter(outStream, "UTF-8"))
            val dataToSend = (URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8")
                    + "&&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8"))
            writer.write(dataToSend)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            Log.d(IO_EXCEPTION_TAG, "sendData: " + e.message)
        }
    }
}