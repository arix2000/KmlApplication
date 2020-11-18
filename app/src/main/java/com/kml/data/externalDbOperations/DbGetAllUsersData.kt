package com.kml.data.externalDbOperations

import java.net.HttpURLConnection

class DbGetAllUsersData : ExternalDbHelper() {
    private val fileName = "getAllDataAboutUser.php"
    private var address = BASE_URL + fileName
    var result: String = ""
        get() {
            join(); return field
        }
    var conn: HttpURLConnection? = null

    override fun run() {

        conn = setConnection(address)
        val dataFromDb: String = readResult(conn!!)
        result = dataFromDb

    }
}