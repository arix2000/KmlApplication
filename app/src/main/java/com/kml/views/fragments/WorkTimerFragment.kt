package com.kml.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.services.TimerService
import com.kml.views.activities.MainActivity
import com.kml.views.dialogs.AddWorkDialog
import com.kml.views.dialogs.InstantAddWorkDialog
import com.kml.views.dialogs.MakeSureDialog
import com.kml.views.dialogs.RestoreDialog
import java.util.*

class WorkTimerFragment : Fragment() {
    private lateinit var root: View
    private lateinit var timerCircle: ImageView
    private lateinit var btnStartWork: ImageButton
    private lateinit var btnEndWork: ImageButton
    private lateinit var btnResetWork: ImageButton
    private lateinit var btnAddWork: ImageButton
    private lateinit var circleAnim: Animation
    private lateinit var textViewSeconds: TextView
    private lateinit var textViewMinutes: TextView
    private lateinit var textViewHours: TextView
    private lateinit var countingThread: CountingThread
    private lateinit var fileFactory: FileFactory
    private lateinit var workName: String
    private lateinit var workDescription: String
    private lateinit var secondsFormatted: String
    private lateinit var minutesFormatted: String
    private lateinit var hoursFormatted: String
    private var minutes = 0
    private var seconds = 0
    private var hours = 0
    private var lastClickTime: Long = 0
    private var isTimerRunning = false
    private var isThreadAlive = false
    private var exitThread = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_work_timer, container, false)
        isTimerRunning = false
        timerCircle = root.findViewById(R.id.timer_circle)
        circleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.timer_circle_anim)
        textViewSeconds = root.findViewById(R.id.timer_counter_seconds)
        textViewMinutes = root.findViewById(R.id.timer_counter_minutes)
        textViewHours = root.findViewById(R.id.timer_counter_hours)
        fileFactory = FileFactory(root.getContext())
        btnAddWork = root.findViewById(R.id.btn_add_work)
        btnAddWork.setOnClickListener(View.OnClickListener { view: View? -> showDialogToInstantAddWork() })
        btnStartWork = root.findViewById(R.id.btn_start_work)
        btnStartWork.setOnClickListener { view: View? ->
            //prevent double click
            if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                return@setOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()
            if (!isTimerRunning) {
                if (MainActivity.isFirstClick) {
                    if (fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT).contains(";")) {
                        showDialogToRestore()
                    } else startCounting()
                    MainActivity.isFirstClick = false
                } else {
                    startCounting()
                }
            } else {
                pauseCounting()
            }
        }
        btnEndWork = root.findViewById(R.id.btn_end_work)
        btnEndWork.setOnClickListener {
            if (hours == 0 && minutes < 1) {
                Toast.makeText(context, R.string.no_minute_counted, Toast.LENGTH_SHORT).show()
            } else showDialogToSetWork()
        }
        btnResetWork = root.findViewById(R.id.btn_reset_work)
        btnResetWork.setOnClickListener { if (seconds != 0 || minutes != 0 || hours != 0) showDialogToMakeSure() }
        return root
    }

    override fun onResume() {
        returnStateFromService()
        setTimeOnLayout()
        root.context.stopService(Intent(root.context, TimerService::class.java))
        if (TimerService.wasPlayClicked) {
            startCounting()
        }
        TimerService.exitServiceThread = true
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(root.context, TimerService::class.java)
        TimerService.wasPlayClicked = isTimerRunning
        exitThread = true
        saveStateToService()
        if (isTimerRunning) {
            TimerService.exitServiceThread = false
            root.context.startService(intent)
        }
    }

    override fun onStop() {
        if (!isTimerRunning && hours > 0 || minutes > 0 || seconds >= 10) {
            fileFactory.saveStateToFile("$seconds;$minutes;$hours", FileFactory.CURRENT_TIME_TXT)
        }
        super.onStop()
    }

    private fun startCounting() {
        btnStartWork.setImageResource(R.drawable.ic_pause)
        exitThread = false
        countingThread = CountingThread()
        timerCircle.startAnimation(circleAnim)
        isTimerRunning = true
        countingThread.start()
        fileFactory.clearFileState(FileFactory.CURRENT_TIME_TXT)
    }

    private fun pauseCounting() {
        btnStartWork.setImageResource(R.drawable.ic_play)
        exitThread = true
        timerCircle.clearAnimation()
        isTimerRunning = false
    }

    private fun resetCounting() {
        writeTimeOnLayout("00", "00:", "00:")
        seconds = 0
        minutes = 0
        hours = 0
        pauseCounting()
        fileFactory.clearFileState(FileFactory.CURRENT_TIME_TXT)
    }

    private fun writeTimeOnLayout(secondsFormatted: String, minutesFormatted: String, hoursFormatted: String) {
        textViewSeconds.text = secondsFormatted
        textViewMinutes.text = minutesFormatted
        textViewHours.text = hoursFormatted
    }

    private fun setTimeOnLayout() {
        secondsFormatted = if (seconds < 10) {
            "0$seconds"
        } else if (seconds == 60) {
            "00"
        } else {
            seconds.toString()
        }
        minutesFormatted = if (minutes < 10) {
            "0$minutes:"
        } else if (minutes == 60) {
            "00:"
        } else {
            "$minutes:"
        }
        hoursFormatted = if (hours < 10) {
            "0$hours:"
        } else if (hours == 60) {
            "00:"
        } else {
            "$hours:"
        }
        writeTimeOnLayout(secondsFormatted, minutesFormatted, hoursFormatted)
    }

    private fun showDialogToSetWork() {
        val dialog = AddWorkDialog(hours, minutes)
        dialog.setOnAcceptListener { resetCounting() }
        dialog.show(parentFragmentManager, "AddWork")
    }


    private fun showDialogToInstantAddWork() {
        val dialog = InstantAddWorkDialog()
        dialog.show(parentFragmentManager, "InstantAddWork")
    }

    private fun showDialogToRestore() {
        val dialog = RestoreDialog()
        dialog.setOnAcceptListener { setTimeFromFile(); startCounting() }
        dialog.setOnCancelListener { startCounting() }
        dialog.show(parentFragmentManager, "Restore")

    }

    private fun showDialogToMakeSure() {
        val dialog = MakeSureDialog()
        dialog.setOnAcceptListener { resetCounting() }
        dialog.show(parentFragmentManager, "MakeSureDialog")

    }

    private fun setTimeFromFile() {
        val fromFile = fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT)
        val hms = fromFile.split(";".toRegex()).toTypedArray()
        seconds = hms[0].toInt()
        minutes = hms[1].toInt()
        hours = hms[2].toInt()
        setTimeOnLayout()
    }

    private fun returnStateFromService() {
        seconds = TimerService.seconds
        minutes = TimerService.minutes
        hours = TimerService.hours
    }

    private fun saveStateToService() {
        TimerService.seconds = seconds
        TimerService.minutes = minutes
        TimerService.hours = hours
    }

    inner class CountingThread : Thread() {
        private val handler = Handler(Looper.getMainLooper())
        override fun run() {
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
                        handler.post { setTimeOnLayoutRealTime() }
                        seconds += 1
                    }
                }
            }, 0, 1000)
        }
    }

    private fun setTimeOnLayoutRealTime() {
        secondsFormatted = if (seconds < 10) {
            "0$seconds"
        } else if (seconds == 60) {
            "00"
        } else {
            seconds.toString()
        }
        textViewSeconds.text = secondsFormatted
        if (seconds >= 60) {
            minutes += 1
            minutesFormatted = if (minutes < 10) {
                "0$minutes:"
            } else if (minutes == 60) {
                "00:"
            } else {
                "$minutes:"
            }
            textViewMinutes.text = minutesFormatted
            seconds = 0
        }
        if (minutes >= 60) {
            hours += 1
            hoursFormatted = if (hours < 10) {
                "0$hours:"
            } else {
                "$hours:"
            }
            textViewHours.text = hoursFormatted
            minutes = 0
        }
    }
}