package com.kml.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.externalDbOperations.DbSendWork
import com.kml.data.models.WorkToAdd
import com.kml.data.services.TimerService
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
        circleAnim = AnimationUtils.loadAnimation(root.getContext(), R.anim.timer_circle_anim)
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
        btnEndWork.setOnClickListener { view: View? ->
            if (hours == 0 && minutes < 1) {
                Toast.makeText(context, R.string.no_minute_counted, Toast.LENGTH_SHORT).show()
            } else showDialogToSetWork()
        }
        btnResetWork = root.findViewById(R.id.btn_reset_work)
        btnResetWork.setOnClickListener({ view: View? -> if (seconds != 0 || minutes != 0 || hours != 0) showDialogToMakeSure() })
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
        val dialog = Dialog(root.context)
        dialog.setContentView(R.layout.dialog_new_work)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val workNameEditText = dialog.findViewById<EditText>(R.id.dialog_timer_work_name_instant)
        val workDescriptionEditText = dialog.findViewById<EditText>(R.id.dialog_timer_work_description_instant)
        val cancel = dialog.findViewById<Button>(R.id.dialog_timer_cancel)
        cancel.setOnClickListener { dialog.dismiss() }
        val confirm = dialog.findViewById<Button>(R.id.dialog_timer_confirm)
        confirm.setOnClickListener {
            workName = workNameEditText.text.toString()
            workDescription = workDescriptionEditText.text.toString()
            if (workName.trim { it <= ' ' }.isEmpty() || workDescription.trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(dialog.context, R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dialog.dismiss()
            val work = WorkToAdd(workName, workDescription, hours, minutes)
            val dbSendWork = DbSendWork(work)
            dbSendWork.start()
            val result = dbSendWork.result
            if (result) {
                Toast.makeText(root.context, R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
                resetCounting()
            } else Toast.makeText(root.context, R.string.adding_work_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialogToInstantAddWork() {
        val dialog = InstantAddWorkDialog()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)


        dialog.show(transaction,"InstantAddWork")
    }

    private fun showDialogToRestore() {
        val dialog = Dialog(root.context)
        dialog.setContentView(R.layout.dialog_restore_from_file)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()
        val btnTak = dialog.findViewById<Button>(R.id.btn_dialog_restore_yes)
        btnTak.setOnClickListener {
            setTimeFromFile()
            dialog.dismiss()
            startCounting()
        }
        val btnNie = dialog.findViewById<Button>(R.id.btn_dialog_restore_no)
        btnNie.setOnClickListener {
            dialog.dismiss()
            startCounting()
        }
    }

    private fun showDialogToMakeSure() {
        val dialog = Dialog(root.context)
        dialog.setContentView(R.layout.dialog_restore_from_file)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val textViewTitle = dialog.findViewById<TextView>(R.id.progress_founded)
        textViewTitle.setText(R.string.warning)
        val textViewQuestion = dialog.findViewById<TextView>(R.id.should_restore)
        textViewQuestion.setText(R.string.reset_confirmation)
        val btnTak = dialog.findViewById<Button>(R.id.btn_dialog_restore_yes)
        btnTak.setText(R.string.reset)
        btnTak.setOnClickListener { view: View? ->
            dialog.dismiss()
            resetCounting()
        }
        val btnNie = dialog.findViewById<Button>(R.id.btn_dialog_restore_no)
        btnNie.setText(R.string.cancel)
        btnNie.setOnClickListener { view: View? -> dialog.dismiss() }
    }

    private fun setTimeFromFile() {
        val fromFile = fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT)
        val HMS = fromFile.split(";".toRegex()).toTypedArray()
        seconds = HMS[0].toInt()
        minutes = HMS[1].toInt()
        hours = HMS[2].toInt()
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