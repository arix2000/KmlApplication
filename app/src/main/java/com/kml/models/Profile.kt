package com.kml.models

import com.kml.data.utilities.FormatEngine

data class Profile(var firstName: String,
                   var lastName: String,
                   var joinYear: String,
                   var timeOfWorkSeason: String,
                   var sections: String,
                   var type: String,
                   var timeOfWorkMonth: String) {

    var fullName: String = ""
        get() {
            return "$firstName $lastName"
        }

    companion object {
        val EMPTY_PROFILE = Profile("", "", "", "", "", "", "")

        fun createFrom(result: String): Profile {
            val format = FormatEngine()
            val splitData = result.split(";".toRegex()).toTypedArray()
            return Profile(splitData[0], splitData[1], splitData[2],
                    format.convertToReadable(splitData[3]),
                    format.formatSections(splitData[4]), splitData[5],
                    format.convertToReadable(splitData[6]))
        }
    }
}