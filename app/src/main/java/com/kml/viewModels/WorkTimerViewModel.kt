package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.models.Time
import com.kml.data.models.WorkToAdd
import com.kml.data.services.TimerService
import com.kml.data.utilities.FileFactory
import com.kml.data.utilities.FormatEngine
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
    private val engine = FormatEngine()

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

    fun setTime(): Time {
        val secondsFormatted = engine.formatSeconds(seconds)
        if (seconds >= 60) {
            minutes += 1
            seconds = 0
        }
        val minutesFormatted = engine.formatMinutes(minutes)

        if (minutes >= 60) {
            hours += 1
            minutes = 0
        }
        val hoursFormatted = engine.formatHours(hours)

        return Time(hoursFormatted, minutesFormatted, secondsFormatted)
    }

    fun setTimeFromFile() {
        val fromFile = repository.readFile()
        val hms = fromFile.split(";".toRegex()).toTypedArray()
        seconds = hms[0].toInt()
        minutes = hms[1].toInt()
        hours = hms[2].toInt()
    }

    fun keepCurrentTime()
    {
        if (!isTimerRunning && hours > 0 || minutes > 0 || seconds >= 10) {
            saveToFile("$seconds;$minutes;$hours")
        }
    }

    private fun saveToFile(toSave: String) {
        repository.saveToFile(toSave)
    }

    fun isFileNotEmpty(): Boolean {
        return repository.readFile().contains(";")
    }

    fun sendWorkToDatabase(work: WorkToAdd): Boolean {
        return repository.addWorkToDatabase(work)
    }

    fun returnStateFromService() {
        seconds = TimerService.seconds
        minutes = TimerService.minutes
        hours = TimerService.hours
    }

    fun saveStateToService() {
        TimerService.seconds = seconds
        TimerService.minutes = minutes
        TimerService.hours = hours
    }

    private val timer = { CoroutineScope(Dispatchers.Default).launch { countTime() } }

    private fun countTime() {
        if (isThreadAlive)
            return

        isThreadAlive = true
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (exitThread) {
                    cancel()
                    isThreadAlive = false
                } else {
                    _seconds.postValue(seconds + 1)
                }
            }
        }, 0, 1000)
    }
}