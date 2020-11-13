package com.kml.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.app.FileFactory
import com.kml.data.models.Time
import com.kml.data.models.WorkToAdd
import com.kml.repositories.WorkTimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WorkTimerViewModel(fileFactory: FileFactory) : ViewModel() {

    private val repository = WorkTimerRepository(fileFactory)

    val _seconds = MutableLiveData<Int>()
    var minutes: Int = 0
    var hours: Int = 0

    var seconds: Int
        get() {
            return _seconds.value ?: -1
        }
        set(value) {
            _seconds.value = value
        }

    var isThreadAlive = false
    var exitThread = false
    var isTimerRunning = false

    fun setTime(): Time {
        val secondsFormatted = formatSeconds(seconds)
        if (seconds >= 60) {
            minutes += 1
            seconds = 0
        }
        val minutesFormatted = formatMinutes(minutes)

        if (minutes >= 60) {
            hours += 1
            minutes = 0
        }
        val hoursFormatted = formatHours(hours)

        return Time(hoursFormatted, minutesFormatted, secondsFormatted)
    }

    fun setTimeFromFile() {
        val fromFile = repository.readFile()
        val hms = fromFile.split(";".toRegex()).toTypedArray()
        seconds = hms[0].toInt()
        minutes = hms[1].toInt()
        hours = hms[2].toInt()
    }

    fun saveToFile(toSave: String) {
        repository.saveToFile(toSave)
    }

    fun startCounting() {
        exitThread = false
        isTimerRunning = true
        timer.invoke()
        repository.clearFileState()
    }

    fun pauseCounting() {
        exitThread = true
        isTimerRunning = false
    }

    fun resetCounting() {
        hours = 0; minutes = 0; seconds = 0
        repository.clearFileState()
    }

    fun isFileNotEmpty(): Boolean {
        return repository.readFile().contains(";")
    }

    fun sendWorkToDatabase(work: WorkToAdd): Boolean {
        return repository.addWorkToDatabase(work)
    }

    private val timer = { CoroutineScope(Dispatchers.Default).launch { countTime() } }

    private fun countTime() {
        if (isThreadAlive) {
            return
        }
        isThreadAlive = true
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (exitThread) {
                    cancel()
                    isThreadAlive = false
                } else {
                    _seconds.postValue(seconds + 1)
                    Log.d("VIEW_MODEL_TAG", "run: $seconds")
                }
            }
        }, 0, 1000)
    }

    private fun formatSeconds(seconds: Int): String =
            when {
                seconds < 10 -> "0$seconds"
                seconds == 60 -> "00"
                else -> seconds.toString()
            }

    private fun formatMinutes(minutes: Int): String =
            when {
                minutes < 10 -> "0$minutes:"
                minutes == 60 -> "00:"
                else -> "$minutes:"
            }

    private fun formatHours(hours: Int): String =
            when {
                hours < 10 -> "0$hours:"
                hours == 60 -> "00:"
                else -> "$hours:"
            }
}



