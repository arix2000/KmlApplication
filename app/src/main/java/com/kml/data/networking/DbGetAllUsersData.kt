package com.kml.data.networking

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kml.models.Volunteer
import java.net.HttpURLConnection

class DbGetAllUsersData : ExternalDbHelper() {
    private val fileName = "getAllDataAboutUser.php"
    private var address = BASE_URL + fileName
    var result: String = ""
        get() {
            join(); return field
        }
    private var conn: HttpURLConnection? = null

    override fun run() {
        conn = setConnection(address)
        val dataFromDb: String = readResult(conn!!)
        result = dataFromDb
    }

    fun syncRun(): List<Volunteer> {
        conn = setConnection(address)
        val dataFromDb: String = readResult(conn!!)
        return createListFromJson(dataFromDb)
    }

    private fun createListFromJson(jsonResult: String): List<Volunteer> {
        val gson = Gson()
        val type = object : TypeToken<List<Volunteer>>() {}.type
        return gson.fromJson(jsonResult, type) ?: emptyList()
    }
}