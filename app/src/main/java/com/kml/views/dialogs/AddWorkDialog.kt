package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.utilities.Vibrator
import com.kml.databinding.DialogNewWorkBinding
import com.kml.extensions.hideSoftKeyboard
import com.kml.extensions.showToast
import com.kml.models.WorkToAdd
import com.kml.viewModels.WorkTimerViewModel

class AddWorkDialog(private val viewModel: WorkTimerViewModel) : AppDialogs(false) {

    lateinit var binding:DialogNewWorkBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        binding = DialogNewWorkBinding.inflate(layoutInflater)
        builder.setView(this.binding.root)

        binding.dialogTimerConfirm.setOnClickListener {
            requireContext().hideSoftKeyboard(it)
            val workName = binding.dialogTimerWorkName.text.toString()
            val workDescription = binding.dialogTimerWorkDescription.text.toString()
            validateAndSend(workName, workDescription)
        }

        binding.dialogTimerCancel.setOnClickListener { dismiss() }

        return builder.create()
    }

    private fun validateAndSend(workName: String, workDescription: String) {

        if (!viewModel.validateWork(workName, workDescription)) {
            showToast(R.string.no_empty_fields)
            return
        }
        binding.worksProgressBar.visibility = View.VISIBLE
        val work = WorkToAdd(workName, workDescription, viewModel.hours, viewModel.minutes)
        viewModel.sendWorkToDatabase(work) {
            resolveResult(it)
        }

    }

    private fun resolveResult(result: Boolean) {
        binding.worksProgressBar.visibility = View.GONE
        if (result) {
            showToast(R.string.adding_work_confirmation)
            onAcceptListener.onAccept()
            dismiss()
            Vibrator(requireContext()).longVibrate()
        } else showToast(R.string.adding_work_error)
    }
}