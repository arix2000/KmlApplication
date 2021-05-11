package com.kml.data.networking

import android.os.Handler
import android.os.Looper
import com.kml.data.listeners.OnResultListener
import com.kml.extensions.logError
import com.kml.extensions.logNetworkResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class ExternalDbHelper : Thread() {

    lateinit var listener: OnResultListener

    companion object {
        const val IO_EXCEPTION_TAG = "IO_EXCEPTION_TAG"
        const val BASE_URL = "http://192.168.1.111/KmlApi2/" //"http://sobos.ssd-linuxpl.com/KmlApi3/"
    }

    protected fun setConnection(address: String): HttpURLConnection? {
        var conn: HttpURLConnection? = null

        try {
            val url = URL(address)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.doInput = true
            conn.requestMethod = "POST"
        } catch (e: Exception) {
            logError(e)
        }

        return conn
    }

    protected fun readResult(conn: HttpURLConnection?): String {
        var readResult = ""
        try {
            val inputStream = conn?.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                readResult += line
            }

            inputStream?.close(); reader.close(); conn?.disconnect()
        } catch (e: Exception) {
            logError(e)
        }
            logNetworkResponse(readResult)
        return readResult
    }

    protected fun invokeOnReceive(result: String) {
        Handler(Looper.getMainLooper()).post { listener.onReceive(result) }
    }

    fun setOnResultListener(operation: (String) -> Unit) {
        this.listener = object : OnResultListener {
            override fun onReceive(result: String) {
                operation(result)
            }
        }
    }
}