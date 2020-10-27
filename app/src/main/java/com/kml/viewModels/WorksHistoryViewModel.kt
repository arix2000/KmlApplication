package com.kml.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.app.FileFactory
import com.kml.data.models.Work
import com.kml.repositories.WorksHistoryRepository
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class WorksHistoryViewModel(fileFactory: FileFactory): ViewModel() {

    private val repository = WorksHistoryRepository(fileFactory)
    val works: MutableLiveData<List<Work>> by lazy {
        MutableLiveData<List<Work>>(createListFromJson(repository.getStringJsonWorks()))
    }

    fun isFromFile():Boolean = repository.isFromFile

    private fun createListFromJson(jsonString: String?): List<Work> {
        var works: List<Work> = ArrayList()
        try {
            val jsonArray = JSONArray(jsonString)
            works = fillArrayFrom(jsonArray)
        } catch (e: JSONException) {
            Log.d("RESULT_FROM_JSON", "onException: " + e.message)
        }
        return works
    }

    @Throws(JSONException::class)
    private fun fillArrayFrom(jsonArray: JSONArray): List<Work> {
        var jsonObject: JSONObject
        val works: MutableList<Work> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            jsonObject = jsonArray.getJSONObject(i)
            works.add(Work(jsonObject.getString("nazwaZadania"), jsonObject.getString("opisZadania"),
                    jsonObject.getString("data"), jsonObject.getString("czasWykonania")))
        }
        return works
    }

}