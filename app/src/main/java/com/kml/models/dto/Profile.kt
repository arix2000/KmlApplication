package com.kml.models.dto

import com.google.gson.annotations.SerializedName
import com.kml.extensions.toReadableTime
import com.kml.utilities.FormatEngine

data class Profile(
    @SerializedName("imie") var firstName: String,
    @SerializedName("nazwisko") var lastName: String,
    @SerializedName("dataDolaczenia") var joinYear: String,
    @SerializedName("czasPracy") var timeOfWorkSeason: String,
    @SerializedName("sekcje") var sections: String,
    @SerializedName("typ") var type: String,
    @SerializedName("czasPracyMiesiac") var timeOfWorkMonth: String
) {

    var fullName: String = ""
        get() {
            return "$firstName $lastName"
        }

    fun withReadableTimes(): Profile {
        return this.apply {
            timeOfWorkMonth = timeOfWorkMonth.toReadableTime()
            timeOfWorkSeason = timeOfWorkSeason.toReadableTime()
        }
    }

    companion object {
        val EMPTY_PROFILE = Profile("", "", "", "", "", "", "")

        fun createFrom(result: String): Profile {
            val format = FormatEngine()
            val splitData = result.split(";".toRegex()).toTypedArray()
            return try {
                Profile(
                    splitData[0], splitData[1], splitData[2],
                    format.convertToReadable(splitData[3]),
                    format.formatSections(splitData[4]), splitData[5],
                    format.convertToReadable(splitData[6])
                )
            } catch (e: Exception) {
                EMPTY_PROFILE
            }

        }
    }
}