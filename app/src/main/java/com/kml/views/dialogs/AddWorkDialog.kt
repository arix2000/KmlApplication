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
import kotlinx.android.synthetic.main.dialog_new_work.view.*

class AddWorkDialog(val hours: Int, val minutes: Int): TimerDialogs() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_new_work, null)
        builder.setView(view)


        view.dialog_timer_confirm.setOnClickListener {
            val workName = view.dialog_timer_work_name.text.toString()
            val workDescription = view.dialog_timer_work_description.text.toString()

            if (workName.trim().isEmpty() || workDescription.trim().isEmpty()) {
                Toast.makeText(requireContext(), R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dismiss()
            val work = WorkToAdd(workName, workDescription, hours, minutes)
            val dbSendWork = DbSendWork(work)
            dbSendWork.start()
            val result = dbSendWork.result
            if (result) {
                Toast.makeText(requireContext(), R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
                onAcceptListener.onAccept()
            } else Toast.makeText(requireContext(), R.string.adding_work_error, Toast.LENGTH_SHORT).show()
        }

        view.dialog_timer_cancel.setOnClickListener { dismiss() }

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}