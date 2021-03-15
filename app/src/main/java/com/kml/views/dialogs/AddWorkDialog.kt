package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.models.WorkToAdd
import com.kml.databinding.DialogNewWorkBinding
import com.kml.viewModels.WorkTimerViewModel

class AddWorkDialog(private val viewModel: WorkTimerViewModel) : AppDialogs(false) {

    lateinit var binding:DialogNewWorkBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        binding = DialogNewWorkBinding.inflate(layoutInflater)
        builder.setView(this.binding.root)

        binding.dialogTimerConfirm.setOnClickListener {
            val workName = binding.dialogTimerWorkName.text.toString()
            val workDescription = binding.dialogTimerWorkDescription.text.toString()
            validateAndSend(workName, workDescription)
        }

        binding.dialogTimerCancel.setOnClickListener { dismiss() }

        return builder.create()
    }

    private fun validateAndSend(workName: String, workDescription: String) {

        if (!viewModel.validateWork(workName, workDescription)) {
            Toast.makeText(requireContext(), R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
            return
        }

        dismiss()
        val work = WorkToAdd(workName, workDescription, viewModel.hours, viewModel.minutes)
        val result = viewModel.sendWorkToDatabase(work)

        resolveResult(result)
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            Toast.makeText(requireContext(), R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
            onAcceptListener.onAccept()
        } else Toast.makeText(requireContext(), R.string.adding_work_error, Toast.LENGTH_SHORT).show()
    }
}