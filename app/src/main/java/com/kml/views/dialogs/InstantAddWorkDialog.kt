package com.kml.views.dialogs


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kml.R
import com.kml.data.externalDbOperations.DbSendWork
import com.kml.data.models.WorkToAdd
import kotlinx.android.synthetic.main.dialog_new_work_instant.view.*


class InstantAddWorkDialog : TimerDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext(), R.style.dialogs_style)
        val view = layoutInflater.inflate(R.layout.dialog_new_work_instant, null)
        builder.setView(view)

        view.apply {
            val button = dialog_timer_add_instant
            button.setOnClickListener {
                val work = WorkToAdd(dialog_timer_work_name_instant.text.toString(),
                        dialog_timer_work_description_instant.text.toString(),
                        dialog_timer_hours.text.toString().toIntOrNull() ?: -1,
                        dialog_timer_minutes.text.toString().toIntOrNull() ?: -1
                )
                sendWorkToDatabase(work)
            }

            dialog_timer_cancel_instant.setOnClickListener {
                dismiss()
            }
        }
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun sendWorkToDatabase(work: WorkToAdd) {

        if (!validation(work))
            return
        dismiss()

        val dbSendWork = DbSendWork(work)
        dbSendWork.start()
        val result = dbSendWork.result

        if (result) {
            Toast.makeText(requireContext(), R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
        } else Toast.makeText(requireContext(), R.string.adding_work_error, Toast.LENGTH_SHORT).show()
    }

    private fun validation(work: WorkToAdd): Boolean {
        return when {
            (isPoolsEmpty(work)) -> {
                Toast.makeText(requireContext(), R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
                false
            }
            (work.minutes > 60) -> {
                Toast.makeText(requireContext(), R.string.too_many_minutes, Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun isPoolsEmpty(work: WorkToAdd): Boolean {
        return work.name.trim().isEmpty() || work.description.trim().isEmpty()
                || work.hours == -1 || work.minutes == -1
    }


}