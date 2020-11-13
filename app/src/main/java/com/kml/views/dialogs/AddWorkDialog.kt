package com.kml.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.app.AppDialogs
import com.kml.data.models.WorkToAdd
import com.kml.viewModels.WorkTimerViewModel
import kotlinx.android.synthetic.main.dialog_new_work.view.*

class AddWorkDialog(private val viewModel: WorkTimerViewModel) : AppDialogs() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        val view = layoutInflater.inflate(R.layout.dialog_new_work, null)
        builder.setView(view)

        view.dialog_timer_confirm.setOnClickListener {
            val workName = view.dialog_timer_work_name.text.toString()
            val workDescription = view.dialog_timer_work_description.text.toString()
            validateAndSend(workName, workDescription)
        }

        view.dialog_timer_cancel.setOnClickListener { dismiss() }

        return builder.create()
    }

    private fun validateAndSend(workName: String, workDescription: String) {

        if (validation(workName, workDescription))
            return

        dismiss()
        val work = WorkToAdd(workName, workDescription, viewModel.hours, viewModel.minutes)
        val result = viewModel.sendWorkToDatabase(work)

        resolveResult(result)
    }

    private fun validation(workName: String, workDescription: String): Boolean {
        return if (workName.trim().isEmpty() || workDescription.trim().isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            Toast.makeText(requireContext(), R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
            onAcceptListener.onAccept()
        } else Toast.makeText(requireContext(), R.string.adding_work_error, Toast.LENGTH_SHORT).show()
    }
}