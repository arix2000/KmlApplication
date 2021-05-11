package com.kml.models

import android.os.Parcelable
import android.text.format.DateFormat
import com.google.gson.annotations.SerializedName
import com.kml.Constants.Date.CREATION_DATE_FORMAT
import com.kml.Constants.Date.NEW_DATE_OUTPUT_FORMAT
import com.kml.Constants.Strings.SPACE_CHAR
import com.kml.extensions.logError
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
open class Work(@SerializedName("imie") val firstName: String?,
                @SerializedName("nazwisko") val lastName: String?,
                @SerializedName("nazwaZadania") val workName: String,
                @SerializedName("opisZadania") val workDescription: String,
                @SerializedName("data") val workDate: String,
                @SerializedName("czasWykonania") val executionTime: String,
                @SerializedName("osoby") val people: String?,
                @SerializedName("typSpotkania") val type: String?
) : Parcelable {
    fun isEmpty() = firstName.isNullOrBlank() && lastName.isNullOrBlank()
            && workName.isBlank() && workDescription.isBlank() && workDate.isBlank()
            && executionTime.isBlank() && people.isNullOrBlank()

    fun isExecutionMonthEquals(month: String): Boolean {
        val executionMonth = DateFormat.format("M", getExecutionDate())
        return executionMonth == month
    }

    fun isExecutionYearEquals(year: String): Boolean {
        val executionYear = DateFormat.format("yyyy", getExecutionDate())
        return executionYear == year
    }

    private fun getExecutionDate(): Date {
        workDescription.trim().run {
            val dateStr = if (this.contains(SPACE_CHAR))
                removeRange(indexOfFirst { it == SPACE_CHAR }, length)
            else workDate

            return getDateValidFrom(dateStr)
        }
    }

    private fun getDateValidFrom(dateStr: String) =
            try {
                getDateFormat(dateStr).parse(dateStr.trim()) ?: Date(0)
            } catch (e: Exception) {
                logError(Throwable("date can't be parsed: \"$dateStr\" \n changing source to creationDate.."))
                getDateFormat(workDate).parse(workDate.trim()) ?: Date(0)
            }

    private fun getDateFormat(date: String) =
            if (date.contains('.'))
                SimpleDateFormat(NEW_DATE_OUTPUT_FORMAT, Locale.getDefault())
            else
                SimpleDateFormat(CREATION_DATE_FORMAT, Locale.getDefault())
}