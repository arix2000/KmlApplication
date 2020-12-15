package com.kml.data.externalDbOperations

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kml.data.listeners.OnResultListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class ExternalDbHelper : Thread() {

    private lateinit var listener: OnResultListener

    companion object {
        const val IO_EXCEPTION_TAG = "IO_EXCEPTION_TAG"
        const val BASE_URL = ""
    }

    protected fun setConnection(address: String): HttpURLConnection? {
        var conn: HttpURLConnection? = null

        try {
            val url = URL(address)
            conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.doInput = true
            conn.requestMethod = "POST"
        } catch (e: IOException) {
            Log.d(IO_EXCEPTION_TAG, "setConnection: " + e.message)
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
        } catch (e: IOException) {
            Log.d(IO_EXCEPTION_TAG, "readResult: " + e.message)
        }

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