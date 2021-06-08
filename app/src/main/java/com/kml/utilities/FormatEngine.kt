package com.kml.utilities

import com.kml.extensions.logError
import kotlin.math.abs
import kotlin.math.roundToInt

class FormatEngine {

    fun convertToReadable(timeOfWork: String): String {
        val convertedTime: String
        try {
            var workTimeFloat = timeOfWork.toFloat()
            val hours = workTimeFloat.toInt()
            workTimeFloat -= hours
            workTimeFloat = abs(workTimeFloat)

            val helpingInteger = (workTimeFloat * 100).roundToInt()
            workTimeFloat = helpingInteger.toFloat() / 100

            val minutes = (workTimeFloat * 60).roundToInt()
            convertedTime = "$hours godz $minutes min"
        } catch (e: NumberFormatException) {
            logError(e)
            return timeOfWork
        }

        return convertedTime
    }

    fun formatSections(sections: String): String {
        var changedSection = sections.replace("-".toRegex(), ",")

        changedSection.apply {
            if (contains("Wolontariusz") && contains("Lider"))
                changedSection = "${substring(0, indexOf("Wolontariusz"))} \n\n" +
                        substring(indexOf("Wolontariusz")).trimIndent()
        }
        return changedSection
    }

    fun formatSeconds(seconds: Int): String =
            when {
                seconds < 10 -> "0$seconds"
                seconds == 60 -> "00"
                else -> seconds.toString()
            }

    fun formatMinutes(minutes: Int): String =
            when {
                minutes < 10 -> "0$minutes:"
                minutes == 60 -> "00:"
                else -> "$minutes:"
            }

    fun formatHours(hours: Int): String =
            when {
                hours < 10 -> "0$hours:"
                hours == 60 -> "00:"
                else -> "$hours:"
            }
}