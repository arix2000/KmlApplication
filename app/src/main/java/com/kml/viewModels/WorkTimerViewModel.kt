package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.models.Time
import com.kml.data.models.WorkToAdd
import com.kml.data.services.TimerService
import com.kml.data.utilities.FileFactory
import com.kml.data.utilities.FormatEngine
import com.kml.data.utilities.Signal
import com.kml.repositories.WorkTimerRepository
import com.kml.views.dialogs.InstantAddWorkDialog.Companion.TODAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WorkTimerViewModel(fileFactory: FileFactory) : ViewModel() {

    private val repository = WorkTimerRepository(fileFactory)

    val seconds = MutableLiveData<Int>()
    var minutes: Int = 0
    var hours: Int = 0

    private var lastClickTime: Long = 0
    private val engine = FormatEngine()

    var secondsValue: Int
        get() {
            return seconds.value ?: -1
        }
        set(value) {
            seconds.value = value
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
        hours = 0; minutes = 0; secondsValue = 0
        repository.clearFileState()
    }

    fun getTime(): Time {
        val secondsFormatted = engine.formatSeconds(secondsValue)
        if (secondsValue >= 60) {
            minutes += 1
            secondsValue = 0
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
        secondsValue = hms[0].toInt()
        minutes = hms[1].toInt()
        hours = hms[2].toInt()
    }

    fun keepCurrentTime()
    {
        if (!isTimerRunning && hours > 0 || minutes > 0 || secondsValue >= 10) {
            saveToFile("$secondsValue;$minutes;$hours")
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
        secondsValue = TimerService.seconds
        minutes = TimerService.minutes
        hours = TimerService.hours
    }

    fun saveStateToService() {
        TimerService.seconds = secondsValue
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
                    seconds.postValue(secondsValue + 1)
                }
            }
        }, 0, 1000)
    }

    fun decideAboutDate(date: String): String {
        return if(date == TODAY)
            getTodayDate()
        else date
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.apply {
            return get(Calendar.DAY_OF_MONTH).toString() + "." +
                    get(Calendar.MONTH) + "." +
                    get(Calendar.YEAR)
        }
    }

    fun validateWork(workName: String, workDescription: String): Boolean {
        return !(workName.trim().isEmpty() || workDescription.trim().isEmpty())
    }

    fun validateWorkInstant(work: WorkToAdd): Int {
        return when {
            (isPoolsEmpty(work)) ->  Signal.EMPTY_POOLS
            (work.minutes > 60) ->  Signal.MANY_MINUTES
            else -> Signal.VALIDATION_SUCCESSFUL
        }
    }

    private fun isPoolsEmpty(work: WorkToAdd): Boolean {
        return work.name.trim().isEmpty() || work.description.trim().isEmpty()
                || work.hours == -1 || work.minutes == -1
    }
}