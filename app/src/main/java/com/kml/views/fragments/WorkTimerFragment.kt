package com.kml.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.services.TimerService
import com.kml.databinding.FragmentWorkTimerBinding
import com.kml.viewModelFactories.WorkTimerViewModelFactory
import com.kml.viewModels.WorkTimerViewModel
import com.kml.views.activities.MainActivity
import com.kml.views.dialogs.AddWorkDialog
import com.kml.views.dialogs.InstantAddWorkDialog
import com.kml.views.dialogs.MakeSureDialog
import com.kml.views.dialogs.RestoreDialog

class WorkTimerFragment : Fragment() {
    private lateinit var binding: FragmentWorkTimerBinding
    private lateinit var viewModel: WorkTimerViewModel
    private lateinit var fileFactory: FileFactory

    private var lastClickTime: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_timer, container, false)

        fileFactory = FileFactory(requireContext())

        val viewModelFactory = WorkTimerViewModelFactory(fileFactory)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkTimerViewModel::class.java)

        viewModel._seconds.observe(viewLifecycleOwner) {
            setTimeOnLayout()
        }

        binding.btnAddWork.setOnClickListener { showDialogToInstantAddWork() }
        binding.btnStartWork.setOnClickListener {
            startOrStopCounter()
        }

        binding.btnEndWork.setOnClickListener {
            if (viewModel.hours == 0 && viewModel.minutes < 1) {
                Toast.makeText(context, R.string.no_minute_counted, Toast.LENGTH_SHORT).show()
            } else showDialogToSetWork()
        }
        binding.btnResetWork.setOnClickListener {
            if (viewModel.seconds != 0 || viewModel.minutes != 0 || viewModel.hours != 0) showDialogToMakeSure()
        }

        return binding.root
    }

    private fun setTimeOnLayout() {
        val time = viewModel.setTime()
        writeTimeOnLayout(time.seconds, time.minutes, time.hours)
    }

    private fun startOrStopCounter() {
        //prevent double click
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        if (!viewModel.isTimerRunning) {
            if (MainActivity.isFirstClick) {
                if (viewModel.isFileNotEmpty()) {
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

    private fun startCounting() {
        binding.btnStartWork.setImageResource(R.drawable.ic_pause)
        val circleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.timer_circle_anim)
        binding.timerCircle.startAnimation(circleAnim)
        viewModel.startCounting()

    }

    private fun pauseCounting() {
        binding.btnStartWork.setImageResource(R.drawable.ic_play)
        binding.timerCircle.clearAnimation()
        viewModel.pauseCounting()
    }

    private fun resetCounting() {
        writeTimeOnLayout("00", "00:", "00:")
        viewModel.resetCounting()
        pauseCounting()
    }

    private fun writeTimeOnLayout(secondsFormatted: String, minutesFormatted: String, hoursFormatted: String) {
        binding.timerCounterSeconds.text = secondsFormatted
        binding.timerCounterMinutes.text = minutesFormatted
        binding.timerCounterHours.text = hoursFormatted
    }

    private fun showDialogToSetWork() {
        val dialog = AddWorkDialog(viewModel)
        dialog.setOnAcceptListener { resetCounting() }
        dialog.show(parentFragmentManager, "AddWork")
    }


    private fun showDialogToInstantAddWork() {
        val dialog = InstantAddWorkDialog(viewModel)
        dialog.show(parentFragmentManager, "InstantAddWork")
    }

    private fun showDialogToRestore() {
        val dialog = RestoreDialog()
        dialog.setOnAcceptListener { viewModel.setTimeFromFile(); startCounting() }
        dialog.setOnCancelListener { startCounting() }
        dialog.show(parentFragmentManager, "Restore")

    }

    private fun showDialogToMakeSure() {
        val dialog = MakeSureDialog()
        dialog.setOnAcceptListener { resetCounting() }
        dialog.show(parentFragmentManager, "MakeSureDialog")

    }

    override fun onResume() {
        returnStateFromService()
        requireContext().stopService(Intent(requireContext(), TimerService::class.java))
        if (TimerService.wasPlayClicked) {
            startCounting()
        }
        TimerService.exitServiceThread = true
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(requireContext(), TimerService::class.java)
        TimerService.wasPlayClicked = viewModel.isTimerRunning
        viewModel.exitThread = true
        saveStateToService()
        if (viewModel.isTimerRunning) {
            TimerService.exitServiceThread = false
            requireContext().startService(intent)
        }
    }

    override fun onStop() {
        if (!viewModel.isTimerRunning && viewModel.hours > 0 || viewModel.minutes > 0 || viewModel.seconds >= 10) {
            viewModel.saveToFile("${viewModel.seconds};${viewModel.minutes};${viewModel.hours}")
        }
        super.onStop()
    }

    private fun returnStateFromService() {
        viewModel.seconds = TimerService.seconds
        viewModel.minutes = TimerService.minutes
        viewModel.hours = TimerService.hours
    }

    private fun saveStateToService() {
        TimerService.seconds = viewModel.seconds
        TimerService.minutes = viewModel.minutes
        TimerService.hours = viewModel.hours
    }

}